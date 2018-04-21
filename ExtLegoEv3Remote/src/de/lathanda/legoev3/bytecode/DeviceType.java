package de.lathanda.legoev3.bytecode;

public interface DeviceType {
	byte UNKNOWN           = (byte)0x01; 
	byte NXT_TOUCH         = (byte)0x01; 
	byte NXT_LIGHT         = (byte)0x02; 
	byte NXT_SOUND         = (byte)0x03; 
	byte NXT_COLOR         = (byte)0x04; 
	byte NXT_ULTRASONIC    = (byte)0x05;
	byte NXT_TEMPERATURE   = (byte)0x06;
	byte L_MOTOR           = (byte)0x07; 
	byte M_MOTOR           = (byte)0x08;
	byte TOUCH             = (byte)0x10; 
	byte TEST              = (byte)0x15; 
	byte I2C               = (byte)0x64; 
	byte NXT_TEST          = (byte)0x65; 
	byte COLOR             = (byte)0x1D; 
	byte ULTRASONIC        = (byte)0x1E; 
	byte GYRO              = (byte)0x20; 
	byte IR                = (byte)0x21;  
	byte NONE              = (byte)0x7E;
}
