package de.lathanda.mindstorm.types;

public class LC0 implements Type {
	private static final byte LC0           = (byte) 0b00000000; // 6 bit number (bits 2..7)	
	private int value; 
	public LC0(byte[] data) throws CorruptedDataException {
		if ((data[0] & 0b11000000) != 0) {
			throw new CorruptedDataException();
		}
		value = ((data[0] & 0b00100000) == 0)?1:-1 * (data[0] & 0b00011111);
	} 
	public LC0(int value) {
		this.value = value;
	}

	@Override
	public byte[] encode() {
		byte[] code = new byte[1];
		code[0] = (byte) ((LC0 | (value & 0b00011111)) | ((value < 0)?0b00100000:0b00000000));
		return code;
	}

}
