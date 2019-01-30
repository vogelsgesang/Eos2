package de.lathanda.mindstorm.types;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class LC2 implements Type {
	private static final byte LC2           = (byte) 0b10000010; // follow 16 bit number little endian 
	private short value; 
	public LC2(byte[] data) throws CorruptedDataException {
		if (data[0] != LC2) {
			throw new CorruptedDataException();
		}		
		ByteBuffer code = ByteBuffer.allocate(3);
		code.put(data);
		code.order(ByteOrder.LITTLE_ENDIAN);
		value = code.getShort(1);
	} 
	public LC2(short value) {
		this.value = value;
	}

	@Override
	public byte[] encode() {
		ByteBuffer code = ByteBuffer.allocate(3);
		code.order(ByteOrder.LITTLE_ENDIAN);
		code.put(LC2);
		code.putShort(value);
		return code.array();
	}

}
