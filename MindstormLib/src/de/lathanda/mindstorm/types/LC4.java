package de.lathanda.mindstorm.types;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LC4 implements Type {
	private static final byte LC4           = (byte) 0b10000011; // follow 32 bit number little endian
	private int value; 
	public LC4(byte[] data) throws CorruptedDataException {
		if (data[0] != LC4) {
			throw new CorruptedDataException();
		}		
		ByteBuffer code = ByteBuffer.allocate(5);
		code.put(data);
		code.order(ByteOrder.LITTLE_ENDIAN);
		value = code.getInt(1);
	} 
	public LC4(int value) {
		this.value = value;
	}

	@Override
	public byte[] encode() {
		ByteBuffer code = ByteBuffer.allocate(3);
		code.order(ByteOrder.LITTLE_ENDIAN);
		code.put(LC4);
		code.putInt(value);
		return code.array();
	}

}
