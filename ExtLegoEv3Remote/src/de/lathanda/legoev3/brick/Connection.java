package de.lathanda.legoev3.brick;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.text.MessageFormat;

import de.lathanda.legoev3.Ev3NotFoundException;
import de.lathanda.legoev3.Ev3SeekerThread;
import de.lathanda.legoev3.comm.Identification;

public class Connection {	
	private static Ev3SeekerThread seeker = Ev3SeekerThread.getInstance();
	private static String CONNECT_MSG = "GET /target?sn= {0} VMTP1.0\n	Protocol: EV3";
	private Socket ev3conn; 
	private PrintStream ev3out;
	private InputStream ev3in;
	private ReplyReceiver rr;
	public void connect() throws IOException {
		Identification ev3 = seeker.getAnyEv3();
		if (ev3 == null) {
			throw new Ev3NotFoundException();
		}
		connect(ev3);
	}
	public void connect(String id) throws IOException {
		Identification ev3 = seeker.seekEv3(id);
		if (ev3 == null) {
			throw new Ev3NotFoundException(id);
		}
		connect(ev3);
	}
	public void connect(Identification ev3) throws IOException {
		String msg = MessageFormat.format(CONNECT_MSG, ev3.getSn());
		ev3conn = new Socket(ev3.getInetadress(), ev3.getPort());
		ev3out = new PrintStream(ev3conn.getOutputStream());
		ev3in = ev3conn.getInputStream();
		ev3out.println(msg);
		rr = new ReplyReceiver();
		new Thread(rr).start();		
	}
	private class ReplyReceiver implements Runnable {
		private static final int LF = 10;
		private volatile boolean running = true;
		private ReplyReceiver() throws IOException {
			StringBuilder answer = new StringBuilder();
			int data = 0;
			while ((data = ev3in.read()) != LF) {
				answer.append((char)data);
			}
			System.out.println(answer);
		}
		@Override
		public void run() {
			//read initial message 
			while (running) {
				
			}
		}
		
	}
}
