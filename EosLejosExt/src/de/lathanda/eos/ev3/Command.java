package de.lathanda.eos.ev3;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Command {
	public static final int TURN_LEFT         = 0;
	public static final int TURN_RIGHT        = 1;
	public static final int MOVE_FORWARD      = 2;
	public static final int MOVE_BACKWARD     = 3;
	public static final int HONK              = 4;
	public static final int DISCONNECT        = 5;
	public static final int REQUEST_DISTANCE  = 6;
	public static final int TRANSMIT_DISTANCE = 7;
	public static final int REQUEST_ANGLE     = 8;
	public static final int TRANSMIT_ANGLE    = 9;
		
	private final int code;
	private final int[] data;
	Command(int code, int ... data) {
		this.code = code;
		this.data = data;
	}
	public static Command receive(DataInputStream in) throws IOException {
		int code = in.readInt();
		int length = in.readInt();
		int[] data = new int[length];
		for (int i = 0; i < length; i++) {
			data[i] = in.readInt();
		}
		return new Command(code, data);
	}
	public void send(DataOutputStream out) throws IOException {		
		out.writeInt(code);
		out.writeInt(data.length);
		for (int i = 0; i < data.length; i++) {
			out.writeInt(data[i]);
		}
	}
	public int getCode() {
		return code;
	}
	public int getData(int index) {
		return data[index];
	}	
}
