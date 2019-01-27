package de.lathanda.mindstorm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Ev3Connection {
	private Ev3ID ev3;
	private InputStream in;
	private OutputStream out;
	private Socket socket;

	public Ev3Connection(Ev3ID ev3) throws IOException {
		this.ev3 = ev3;
		ev3.getDiscover().initConnection(ev3);

		socket = new Socket(ev3.getAddress(), ev3.getPort());
		in = socket.getInputStream();
		out = socket.getOutputStream();

		String unlockMessage = "GET /target?sn=" + ev3.getSerialNumber() + "VMTP1.0\n" + "Protocol: "
				+ ev3.getProtocol();
		out.write(unlockMessage.getBytes());

		byte[] answer = new byte[16];
		in.read(answer);
		if (!(new String(answer)).startsWith("Accept:EV340")) {
			throw new IOException(new String(answer));
		}

	}

	private void printHex(String desc, ByteBuffer buffer) {
		System.out.print(desc + " 0x|");
		for (int i = 0; i < buffer.position() - 1; i++) {
			System.out.printf("%02X:", buffer.get(i));
		}
		System.out.printf("%02X|", buffer.get(buffer.position() - 1));
		System.out.println();
	}

	public ByteBuffer sendDirectCmd(ByteBuffer operations, int local_mem, int global_mem) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocateDirect(operations.position() + 7);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort((short) (operations.position() + 5)); // length
		buffer.putShort((short) 42); // counter
		buffer.put(Bytecode.DIRECT_COMMAND_REPLY); // type
		buffer.putShort((short) (local_mem * 1024 + global_mem)); // header
		for (int i = 0; i < operations.position(); i++) { // operations
			buffer.put(operations.get(i));
		}

		byte[] cmd = new byte[buffer.position()];
		for (int i = 0; i < buffer.position(); i++)
			cmd[i] = buffer.get(i);
		out.write(cmd);

		byte[] reply = new byte[global_mem + 5];
		in.read(reply);
		buffer = ByteBuffer.wrap(reply);
		buffer.position(reply.length);

		return buffer;
	}
	public ByteBuffer sendDirectCmd(ByteBuffer operations) throws IOException {
		return sendDirectCmd(operations, 0, 0);
	}
	public void sendDirectCmdNoReply(ByteBuffer operations, int local_mem, int global_mem) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocateDirect(operations.position() + 7);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		buffer.putShort((short) (operations.position() + 5)); // length
		buffer.putShort((short) 42); // counter
		buffer.put(Bytecode.DIRECT_COMMAND_NO_REPLY); // type
		buffer.putShort((short) (local_mem * 1024 + global_mem)); // header
		for (int i = 0; i < operations.position(); i++) { // operations
			buffer.put(operations.get(i));
		}

		byte[] cmd = new byte[buffer.position()];
		for (int i = 0; i < buffer.position(); i++)
			cmd[i] = buffer.get(i);
		out.write(cmd);
	}
	public void sendDirectCmdNoReply(ByteBuffer operations) throws IOException {
		sendDirectCmdNoReply(operations, 0, 0);
	}
}
