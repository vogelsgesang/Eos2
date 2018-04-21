package de.lathanda.legoev3.comm;

import java.net.InetAddress;

public class Identification {
	private final String sn;
	private InetAddress inetadress;
	private String name;
	private int port;
	private String protocol;
	public Identification(String sn) {
		super();
		this.sn = sn;
		this.inetadress = null;
		this.name = "EV3";
		this.port = 5555;
		this.protocol = "EV3";
	}
	public InetAddress getInetadress() {
		return inetadress;
	}
	public void setInetadress(InetAddress inetadress) {
		this.inetadress = inetadress;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSn() {
		return sn;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getProtocol() {
		return protocol;
	}
	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}
	
}
