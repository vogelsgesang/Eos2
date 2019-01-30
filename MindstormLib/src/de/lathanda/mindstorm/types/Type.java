package de.lathanda.mindstorm.types;

public interface Type {
	public byte[] encode();
	public static Type decode(byte[] data) throws CorruptedDataException {
		switch (data[0] & 0b11000000) {
		case 0b01000000: //LCS
			return new LCS(data);
		case 0b00000000: //LC0
			return new LC0(data);
		case 0b10000000: //LCx
			switch (data[0] & 0b00000011) {
			case 0b00000001: //LC1
				return new LC1(data);
			case 0b00000010: //LC2
				return new LC2(data);
			case 0b00000011: //LC4
				return new LC4(data);
			}
		}
		throw new CorruptedDataException();
	}
	public static Type createLCX(int value) {
		if (value > -33 && value < 31) {
			return new LC0(value);
		} else if (value > -129 && value < 128) {
			return new LC1((byte)value);
		} else if (value > -32769 && value < 32768) {
			return new LC2((short)value);
		} else {
			return new LC4(value);
		}
	}
}
