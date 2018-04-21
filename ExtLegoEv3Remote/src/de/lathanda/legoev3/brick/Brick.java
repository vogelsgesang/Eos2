package de.lathanda.legoev3.brick;

import java.io.IOException;

import de.lathanda.legoev3.comm.Identification;

public class Brick {
	private final Connection connection;
	private Motor[] motor = new Motor[4];
	private Sensor[] sensor = new Sensor[4];
	private Memory memory = new Memory();
	private FileSystem filesystem = new FileSystem();
	private Mailbox mailbox = new Mailbox();
	private MotorSync motorSync = new MotorSync();
	public Brick() {
		connection = new Connection();
	}
	public void connect() throws IOException {
		connection.connect();		
	}
	public void connect(String id) throws IOException {
		connection.connect(id);
	}
	public void connect(Identification ev3) throws IOException {
		connection.connect(ev3);
	}
	public void connectSensor(Sensor s, int port) {
		sensor[port] = s;
	}
	public void connectMotor(Motor m, int port) {
		motor[port] = m;
	}
}
