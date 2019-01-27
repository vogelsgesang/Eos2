package de.lathanda.mindstorm;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Ev3ID implements Comparable<Ev3ID> {
	private static String BROADCAST_PATTERN = 
			"Serial-Number: (\\w*)\\s\\n" +
            "Port:\\s(\\d{4,4})\\s\\n" +
            "Name:\\s(\\w+)\\s\\n" +
            "Protocol:\\s(\\w+)\\s\\n";	
	private final String name;
	private final String serialNumber;
	private final String protocol;
	private final int port;
	private final InetAddress address;
	private final Discover discover;
	public Ev3ID(DatagramPacket dp, Discover discover) throws NoEv3Exception {
		Pattern broadcastPattern = Pattern.compile(BROADCAST_PATTERN);
		String broadcast = new String(dp.getData());
		Matcher matcher = broadcastPattern.matcher(broadcast);	
		if (matcher.matches()) {
			serialNumber  = matcher.group(1);
			port          = Integer.parseInt(matcher.group(2));
			name          = matcher.group(3);
			protocol      = matcher.group(4);
			address       = dp.getAddress();
			this.discover = discover;
		} else {
			throw new NoEv3Exception();
		}
	}
	public int getPort() {
		return port;
	}
	public String getName() {
		return name;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public String getProtocol() {
		return protocol;
	}
	public InetAddress getAddress() {
		return address;
	}
	@Override
	public int compareTo(Ev3ID o) {
		return serialNumber.compareTo(o.serialNumber);
	}
	public Discover getDiscover() {
		return discover;
	}
}
