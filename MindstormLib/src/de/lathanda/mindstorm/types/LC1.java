package de.lathanda.mindstorm.types;

public class LC1 implements Type {
	private static final byte LC1           = (byte) 0b10000001; // follow 8 bit number
	private byte value; 
	public LC1(byte[] data) throws CorruptedDataException {
		if (data[0] != LC1) {
			throw new CorruptedDataException();
		}
		value = data[1];
	} 
	public LC1(byte value) {
		this.value = value;
	}

	@Override
	public byte[] encode() {
		byte[] code = new byte[2];
		code[0] = LC1;
		code[1] = (byte)value;
		return code;
	}

}
