package de.lathanda.mindstorm;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import de.lathanda.mindstorm.Bytecode.Instruction;
import de.lathanda.mindstorm.Bytecode.SoundCom;
public class Sound {
	private Ev3Connection conn;

	public Sound(Ev3Connection conn) {
		this.conn = conn;
	}
	public void playTone(byte volume, short frequency, short duration) throws IOException {
		if (frequency < 250 || frequency > 10000 || volume < 0 || volume > 100) return;
		ByteBuffer code = ByteBuffer.allocate(7);
		code.order(ByteOrder.LITTLE_ENDIAN);
		code.put(Instruction.SOUND);
		code.put(SoundCom.TONE);
		code.put(volume);
		code.putShort(frequency);
		code.putShort(duration);
		conn.sendDirectCmdNoReply(code);
	}
}
