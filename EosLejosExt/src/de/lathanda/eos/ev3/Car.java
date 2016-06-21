package de.lathanda.eos.ev3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import de.lathanda.eos.base.event.CleanupListener;
import de.lathanda.eos.ev3.exception.ConnectionLostException;
import de.lathanda.eos.ev3.exception.DeviceNotFoundException;

public class Car implements CleanupListener {
	private Socket ev3;
	private String addr;
	private DataInputStream in;
	private DataOutputStream out;

	public Car() {
		addr = "";
	}

	public void connect(String address) {
		InetAddress ip;
		this.addr = address;
		try {
			ip = InetAddress.getByName(address);
		} catch (UnknownHostException e) {
			throw new DeviceNotFoundException(address);
		}
		try {
			ev3 = new Socket(ip, 8777);
			in = new DataInputStream(ev3.getInputStream());
			out = new DataOutputStream(ev3.getOutputStream());
		} catch (IOException e) {
			throw new DeviceNotFoundException(address);
		}
	}

	public void turnLeft(int degree) {
		Command cmd = new Command(Command.TURN_LEFT, degree);
		try {
			cmd.send(out);
		} catch (IOException io) {
			throw new ConnectionLostException();
		}
	}

	public void turnRight(int degree) {
		Command cmd = new Command(Command.TURN_RIGHT, degree);
		try {
			cmd.send(out);
		} catch (IOException io) {
			throw new ConnectionLostException();
		}
	}

	public void driveForward(int distance) {
		Command cmd = new Command(Command.MOVE_FORWARD, distance);
		try {
			cmd.send(out);
		} catch (IOException io) {
			throw new ConnectionLostException();
		}
	}

	public void driveBackward(int distance) {
		Command cmd = new Command(Command.MOVE_BACKWARD, distance);
		try {
			cmd.send(out);
		} catch (Exception io) {
			throw new ConnectionLostException();
		}
	}

	public void honk() {
		Command cmd = new Command(Command.HONK);
		try {
			cmd.send(out);
		} catch (Exception io) {
			throw new ConnectionLostException();
		}
	}

	public int readDistanceSensor() {
		Command cmd = new Command(Command.REQUEST_DISTANCE);
		try {
			cmd.send(out);
			Command answer = Command.receive(in);
			return answer.getData(0);
		} catch (Exception io) {
			throw new ConnectionLostException();
		}
	}
	public int readAngleSensor() {
		Command cmd = new Command(Command.REQUEST_ANGLE);
		try {
			cmd.send(out);
			Command answer = Command.receive(in);
			return answer.getData(0);
		} catch (Exception io) {
			throw new ConnectionLostException();
		}
	}

	@Override
	public String toString() {
		return addr;
	}

	@Override
	public void terminate() {
		if (out != null) {
			Command cmd = new Command(Command.DISCONNECT);
			try {
				cmd.send(out);
			} catch (Throwable t) {
				// if we can't send a shutdown message we can't do anything
				// useful anymore
			}
			try {
				ev3.close();
			} catch (IOException e) {
			}
			out = null;
			in = null;
			ev3 = null;
		}

	}
}
