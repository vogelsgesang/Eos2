package de.lathanda.eos.ev3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Enumeration;
import java.util.LinkedList;

import lejos.hardware.Audio;
import lejos.hardware.Button;
import lejos.hardware.Key;
import lejos.hardware.KeyListener;
import lejos.hardware.ev3.EV3;
import lejos.hardware.ev3.LocalEV3;
import lejos.hardware.lcd.LCD;
import lejos.hardware.motor.Motor;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3GyroSensor;
import lejos.hardware.sensor.EV3UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.SampleProvider;
/**
 * \brief Lejos Server
 * 
 * Der Server warten auf eine Netzwerk verbindung über die 
 * der Roboter ferngesteuert werden kann.
 * 
 * Das Protokoll ist äußerst simple.
 * command(int) anzahl daten(int) (daten(int))*
 * 
 * Jede Methode entspricht genau einem Kommando.
 *
 * @author Peter (Lathanda) Schneider
 */
public class Server implements KeyListener {
	private ServerSocket socket;
	private Socket eos;
	private DataInputStream in;
	private DataOutputStream out;
	private EV3 ev3;
	private RegulatedMotor left;
	private RegulatedMotor right;
	private Audio audio;
	private EV3UltrasonicSensor distance;
	private EV3GyroSensor angle;
	private SampleProvider distanceProvider;
	private SampleProvider angleProvider;
	private volatile boolean running = true;
	private volatile boolean connected = true;
	
	private double forwardCorrection = 2d;
	private double turningCorrection = 2d;
	private LinkedList<String> ips = new LinkedList<>();

	public Server() {
		try {
			ev3 = LocalEV3.get();
			socket = new ServerSocket(8777);
			left = Motor.B;
			right = Motor.C;
			audio = ev3.getAudio();
			Button.ENTER.addKeyListener(this);
			Button.ESCAPE.addKeyListener(this);
			distance = new EV3UltrasonicSensor(SensorPort.S4);
			distanceProvider = distance.getDistanceMode();
			angle = new EV3GyroSensor(SensorPort.S1);
			angleProvider = angle.getAngleMode();
			//determine ip
            Enumeration<NetworkInterface> ifaces = NetworkInterface.getNetworkInterfaces();
            while (ifaces.hasMoreElements()) {
                NetworkInterface ni = ifaces.nextElement();
                Enumeration<InetAddress> addrs = ni.getInetAddresses();
                while (addrs.hasMoreElements()) {
                	InetAddress addr = addrs.nextElement();
                	if (!addr.isLoopbackAddress()) {
                		ips.add(addr.getHostAddress());
                	}
                }
            }			
		} catch (IOException e) {
			System.out.println("Error: " + e.getLocalizedMessage());
			running = false;
		}
	}

	public void start() {
		LCD.clearDisplay();
		while (running) {
			System.out.println("ready");
			for(String ip : ips) {
				System.out.println(ip);
			}
			Button.LEDPattern(1);
			try {				
				eos = socket.accept();
				in = new DataInputStream(eos.getInputStream());
				out = new DataOutputStream(eos.getOutputStream());
				Command cmd = null;
				System.out.println("controlled by");
				Button.LEDPattern(3);
				audio.systemSound(1);
				System.out.println(eos.getInetAddress().toString());
				connected = true;
				while (connected && running) {
					try {
						cmd = Command.receive(in);
						switch (cmd.getCode()) {
						case Command.DISCONNECT:
							connected = false;
						case Command.HONK:
							audio.systemSound(1);
							break;
						case Command.TURN_LEFT:
							left.synchronizeWith(new RegulatedMotor[] { right });
							left.startSynchronization();
							left.rotate((int)(-turningCorrection*cmd.getData(0)), true);
							right.rotate((int)(turningCorrection*cmd.getData(0)));
							left.endSynchronization();
							waitMovementFinished();							
							break;
						case Command.TURN_RIGHT:
							left.synchronizeWith(new RegulatedMotor[] { right });
							left.startSynchronization();
							left.rotate((int)(turningCorrection*cmd.getData(0)), true);
							right.rotate((int)(-turningCorrection*cmd.getData(0)));
							left.endSynchronization();
							waitMovementFinished();
							break;
						case Command.MOVE_FORWARD:
							left.synchronizeWith(new RegulatedMotor[] { right });
							left.startSynchronization();
							left.rotate((int)(forwardCorrection*cmd.getData(0)), true);
							right.rotate((int)(forwardCorrection*cmd.getData(0)));
							left.endSynchronization();
							waitMovementFinished();
							break;
						case Command.MOVE_BACKWARD:
							left.synchronizeWith(new RegulatedMotor[] { right });
							left.startSynchronization();
							left.rotate((int)(-forwardCorrection*cmd.getData(0)), true);
							right.rotate((int)(-forwardCorrection*cmd.getData(0)));
							left.endSynchronization();
							waitMovementFinished();
							break;
						case Command.REQUEST_DISTANCE:
						{
							float[] data = new float[distanceProvider.sampleSize()];
							distanceProvider.fetchSample(data, 0);
							Command answer = new Command(Command.TRANSMIT_DISTANCE, (int) (Math.min(data[0] * 1000, 2000)));
							answer.send(out);
						}
							break;
						case Command.REQUEST_ANGLE:
						{
							float[] data = new float[angleProvider.sampleSize()];
							angleProvider.fetchSample(data, 0);
							Command answer = new Command(Command.TRANSMIT_ANGLE, (int) (data[0]));
							answer.send(out);
						}
							break;
						}
					} catch (Throwable t) {
						if (running) {
							connected = false;
							System.out.println("Error: " + t.getLocalizedMessage());
						}
					}
				}
				System.out.println("disconnected");
			} catch (Throwable t) {
				if (running) {
					System.out.println("Error: " + t.getLocalizedMessage());				
					System.out.println("Client connection failed");
				}
			}
		}
		Button.LEDPattern(0);
	}

	@Override
	public void keyPressed(Key k) {
		switch(k.getId()) {
		case Button.ID_ESCAPE:
			shutdown();
			break;
		}

	}
	private void waitMovementFinished() {
		while(left.isMoving()) {
			synchronized (this) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {}
			}
		}		
	}
	private void shutdown() {
		running = false;
		connected = false;
		System.out.println("manual shutdown");
		try {
			if (in != null) {
				in.close();
			}
			if (eos != null) {
				eos.close();
			}
			if (socket != null) {
				socket.close();
			}
		} catch (IOException e) {}
	}

	@Override
	public void keyReleased(Key k) {
	}

}
