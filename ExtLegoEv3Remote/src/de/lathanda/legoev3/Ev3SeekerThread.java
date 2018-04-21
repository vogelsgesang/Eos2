package de.lathanda.legoev3;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.lathanda.legoev3.comm.Identification;

public class Ev3SeekerThread  implements Runnable {
	private static Ev3SeekerThread seeker;
	private static final Pattern BROADCAST_PATTERN = Pattern.compile(
		"Serial-Number:\\s(\\w+)\\R" +
		"Port:\\s(\\d+)\\R" +
		"Name:\\s(\\w+)\\R" +
		"Protocol:\\s(\\w+)\\R"
	);
	//Example Broadcast
	//Serial-Number: 0016533f0c1e
	//Port: 5555
	//Name: EV3
	//Protocol: EV3
	private volatile boolean running = false;
	private TreeMap<String, Identification> found;
	
	private Ev3SeekerThread() {
		found = new TreeMap<>();
	}
	public static Ev3SeekerThread getInstance() {
		synchronized (Ev3SeekerThread.class) {
			if (seeker == null) {
				seeker = new Ev3SeekerThread();
			}
		}
		return seeker;
	}
	public synchronized boolean isRunning() {
		return running;
	}

	@Override
	public void run() {		
		try (DatagramSocket socket = new DatagramSocket(3015, InetAddress.getByName("0.0.0.0"))) {				
		    socket.setBroadcast(true);
		    byte[] buf = new byte[512]; //67 needed for 3 character name
		    DatagramPacket packet = new DatagramPacket(buf, buf.length);
		    running = true;
		    while (running) {
		      socket.receive(packet);		      
		      evalPacket(new String(packet.getData(), packet.getOffset(), packet.getLength()), packet.getAddress());
		    }			
		} catch (IOException e) {
			running = false;
		}
	}

	private void evalPacket(String broadcastMessage, InetAddress inetaddr) {
		Matcher matcher = BROADCAST_PATTERN.matcher(broadcastMessage);
		if (matcher.matches()) {
			String sn       = matcher.group(1);
			String port     = matcher.group(2);
			String name     = matcher.group(3);
			String protocol = matcher.group(4);
			synchronized (found) {				
				Identification ev3 = found.get(sn);
				if (ev3 == null) {
					ev3 = new Identification(sn);
					found.put(sn, ev3);
				}
				ev3.setInetadress(inetaddr);
				ev3.setPort(Integer.parseInt(port, 10));
				ev3.setName(name);
				ev3.setProtocol(protocol);
			}
		}
	}
	public Identification seekEv3(String target) {
		synchronized(found) {
			for (Identification ev3 : found.values()) {
				if (ev3.getName().equalsIgnoreCase(target) || ev3.getInetadress().getHostAddress().equals(target)) {
					return ev3;
				}
			}
		}
		return null;
	}
	public Identification getAnyEv3() {	
		Entry<String, Identification> first = found.firstEntry();		
		return (first == null)?null:first.getValue();
	}
	public synchronized void start() {
		if (!isRunning()) {
			new Thread(this).start();
		}		
	}
}
