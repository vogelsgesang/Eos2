package de.lathanda.mindstorm;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Test {

	public static void main(String[] args) {
		Discover d = new Discover();
		new Thread(d).start();
		while(d.getDiscoveredEv3().length == 0);
		try {
			Ev3Connection ec =  new Ev3Connection(d.getDiscoveredEv3()[0]);
		    ByteBuffer operations = ByteBuffer.allocateDirect(1);
		    operations.put(Bytecode.Instruction.NOP);

		    ByteBuffer reply = ec.sendDirectCmd(operations, 0, 0);			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
