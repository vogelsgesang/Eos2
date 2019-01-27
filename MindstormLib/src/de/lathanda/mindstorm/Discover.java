package de.lathanda.mindstorm;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.TreeSet;

public class Discover implements Runnable {
	private static int DISCOVER_PORT = 3015;
	private volatile boolean running;
	private TreeSet<Ev3ID> discovered = new TreeSet<>();
	private DatagramSocket listener = null;
	@Override
	public void run() {		
		try {
			listener = new DatagramSocket(DISCOVER_PORT);
			running = true;
			while (running) {
				DatagramPacket packet = new DatagramPacket(new byte[67], 67);
				listener.receive(packet);
				try {
					Ev3ID ev3 = new Ev3ID(packet, this);
					discovered.add(ev3);
				} catch (NoEv3Exception e) {
					//something else sending on this port, ignore it
				}
			}			
		} catch (IOException e) {
		} finally {
			if (listener != null) {
				listener.close();
			}
		}		
	}
	public void stop() {
		running = false;
	}
	public Ev3ID[] getDiscoveredEv3() {
		return discovered.toArray(new Ev3ID[discovered.size()]);
	}
	public void initConnection(Ev3ID ev3) throws IOException {
		listener.connect(ev3.getAddress(), ev3.getPort());
		listener.send(new DatagramPacket(new byte[1], 1));
		listener.close();		
	}
}
