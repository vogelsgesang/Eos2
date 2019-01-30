package de.lathanda.mindstorm.types;

public class LCS implements Type {
	private static final byte LCS           = (byte) 0b01010100; 		
	private static final byte LCS_TERMINATE = (byte) 0b00000000;

	private StringBuffer text;
	public LCS(byte[] data) throws CorruptedDataException {
		text = new StringBuffer();
		if (data[0] != LCS) {
			throw new CorruptedDataException();
		}
		for(int i = 1; i < data.length; i++) {
			if (data[i] != LCS_TERMINATE) {
				text.append((char)data[i]);
			}
		}
	}
	public LCS(String data) {
		this.text = new StringBuffer(data);
	}
	@Override
	public byte[] encode() {
		byte[] code = new byte[text.length() + 2];
		code[0] = LCS;
		code[code.length - 1] = LCS_TERMINATE;
		for(int i = 0; i < text.length(); i++) {
			code[i + 1] = (byte)text.charAt(i);
		}
		return code;
	}
	
}
