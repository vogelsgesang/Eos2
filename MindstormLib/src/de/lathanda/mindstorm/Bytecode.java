package de.lathanda.mindstorm;

public interface Bytecode {
	public interface Instruction {
		byte ERROR             = (byte)0x00;
		byte NOP               = (byte)0x01;
		byte PROGRAM_STOP      = (byte)0x02;
		byte PROGRAM_START     = (byte)0x03;
		byte OBJECT_STOP       = (byte)0x04;
		byte OBJECT_START      = (byte)0x05;
		byte OBJECT_TRIG       = (byte)0x06;
		byte IBJECT_WAIT       = (byte)0x07;
		byte RETURN            = (byte)0x08;
		byte CALL              = (byte)0x09;
		byte OBJECT_END        = (byte)0x0A;
		byte SLEEP             = (byte)0x0B;
		byte PROGRAM_INFO      = (byte)0x0C;
		byte LABEL             = (byte)0x0D;
		byte PROBE             = (byte)0x0E;
		byte DO                = (byte)0x0F;
		byte ADD8              = (byte)0x10;
		byte ADD16             = (byte)0x11;
		byte ADD32             = (byte)0x12;
		byte ADDF              = (byte)0x13;
		byte SUB8              = (byte)0x14;
		byte SUB16             = (byte)0x15;
		byte SUB32             = (byte)0x16;
		byte SUBF              = (byte)0x17;
		byte MUL8              = (byte)0x18;
		byte MUL16             = (byte)0x19;
		byte MUL32             = (byte)0x1A;
		byte MULF              = (byte)0x1B;
		byte DIV8              = (byte)0x1C;
		byte DIV16             = (byte)0x1D;
		byte DIV32             = (byte)0x1E;
		byte DIVF              = (byte)0x1F;
		byte OR8               = (byte)0x20;
		byte OR16              = (byte)0x21;
		byte OR32              = (byte)0x22;
		
		byte AND8              = (byte)0x24;
		byte AND16             = (byte)0x25;
		byte AND32             = (byte)0x26;
		
		byte XOR8              = (byte)0x28;
		byte XOR16             = (byte)0x29;
		byte XOR32             = (byte)0x2A;

		byte RL8               = (byte)0x2C;
		byte RL16              = (byte)0x2D;
		byte RL32              = (byte)0x2E;
		byte INIT              = (byte)0x2F;
		byte MOVE8_8           = (byte)0x30;
		byte MOVE8_16          = (byte)0x31;
		byte MOVE8_32          = (byte)0x32;
		byte MOVE8_F           = (byte)0x33;
		byte MOVE16_8          = (byte)0x34;
		byte MOVE16_16         = (byte)0x35;
		byte MOVE16_32         = (byte)0x36;
		byte MOVE16_F          = (byte)0x37;
		byte MOVE32_8          = (byte)0x38;
		byte MOVE32_16         = (byte)0x39;
		byte MOVE32_32         = (byte)0x3A;
		byte MOVE32_F          = (byte)0x3B;
		byte MOVEF_8           = (byte)0x3C;
		byte MOVEF_16          = (byte)0x3D;
		byte MOVEF_32          = (byte)0x3E;
		byte MOVEF_F           = (byte)0x3F;
		byte JR                = (byte)0x40;
		byte JR_FALSE          = (byte)0x41;
		byte JR_TRUE           = (byte)0x42;
		byte JR_NAN            = (byte)0x43;
		byte CP_LT8            = (byte)0x44;
		byte CP_LT16           = (byte)0x45;
		byte CP_LT32           = (byte)0x46;
		byte CP_LTF            = (byte)0x47;
		byte CP_GT8            = (byte)0x48;
		byte CP_GT16           = (byte)0x49;
		byte CP_GT32           = (byte)0x4A;
		byte CP_GTF            = (byte)0x4B;
		byte CP_EQ8            = (byte)0x4C;
		byte CP_EQ16           = (byte)0x4D;
		byte CP_EQ32           = (byte)0x4E;
		byte CP_EQF            = (byte)0x4F;
		byte CP_NEQ8           = (byte)0x50;
		byte CP_NEQ16          = (byte)0x51;
		byte CP_NEQ32          = (byte)0x52;
		byte CP_NEQF           = (byte)0x53;
		byte CP_LTEQ8          = (byte)0x54;
		byte CP_LTEQ16         = (byte)0x55;
		byte CP_LTEQ32         = (byte)0x56;
		byte CP_LTEQF          = (byte)0x57;
		byte CP_GTEQ8          = (byte)0x58;
		byte CP_GTEQ16         = (byte)0x59;
		byte CP_GTEQ32         = (byte)0x5A;
		byte CP_GTEQF          = (byte)0x5B;
		byte SELECT8           = (byte)0x5C;
		byte SELECT16          = (byte)0x5D;
		byte SELECT32          = (byte)0x5E;
		byte SELECTF           = (byte)0x5F;
		byte SYSTEM            = (byte)0x60;
		byte PORT_CNV_OUTPUT   = (byte)0x61;
		byte PORT_CNV_INPUT    = (byte)0x62;
		byte NOTE_TO_FREQ      = (byte)0x63;
		byte JR_LT8            = (byte)0x64;
		byte JR_LT16           = (byte)0x65;
		byte JR_LT32           = (byte)0x66;
		byte JR_LTF            = (byte)0x67;
		byte JR_GT8            = (byte)0x68;
		byte JR_GT16           = (byte)0x69;
		byte JR_GT32           = (byte)0x6A;
		byte JR_GTF            = (byte)0x6B;
		byte JR_EQ8            = (byte)0x6C;
		byte JR_EQ16           = (byte)0x6D;
		byte JR_EQ32           = (byte)0x6E;
		byte JR_EQF            = (byte)0x6F;
		byte JR_NEQ8           = (byte)0x70;
		byte JR_NEQ16          = (byte)0x71;
		byte JR_NEQ32          = (byte)0x72;
		byte JR_NEQF           = (byte)0x73;
		byte JR_LTEQ8          = (byte)0x74;
		byte JR_LTEQ16         = (byte)0x75;
		byte JR_LTEQ32         = (byte)0x76;
		byte JR_LTEQF          = (byte)0x77;
		byte JR_GTEQ8          = (byte)0x78;
		byte JR_GTEQ16         = (byte)0x79;
		byte JR_GTEQ32         = (byte)0x7A;
		byte JR_GTEQF          = (byte)0x7B;
		byte INFO              = (byte)0x7C;
		byte STRING            = (byte)0x7D;
		byte MEMORY_WRITE      = (byte)0x7E;
		byte MEMORY_READ       = (byte)0x7F;
		byte UI_FLUSH          = (byte)0x80; 
		byte UI_READ           = (byte)0x81; 
		byte UI_WRITE          = (byte)0x82; 
		byte UI_BUTTON         = (byte)0x83;
		byte UI_DRAW           = (byte)0x84; 	
		byte TIMER_WAIT        = (byte)0x85;
		byte TIMER_READY       = (byte)0x86;
		byte TIMER_READ        = (byte)0x87;	
		byte BPO               = (byte)0x88;
		byte BPI               = (byte)0x89;
		byte BP2               = (byte)0x8A;
		byte BP3               = (byte)0x8B;
		byte PB_SET            = (byte)0x8C;
		byte MATH              = (byte)0x8D;
		byte RANDOM            = (byte)0x8E;
		byte TIMER_READ_US     = (byte)0x8F;
		byte KEEP_ALIVE        = (byte)0x90; 
		byte COM_READ          = (byte)0x91; 
		byte COM_WRITE         = (byte)0x92; 

		byte SOUND             = (byte)0x94;
		byte SOUND_TEST        = (byte)0x95;
		byte SOUND_READY       = (byte)0x96;

		byte INPUT_DEVICE_LIST = (byte)0x98;
		byte INPUT_DEVICE      = (byte)0x99;
		byte INPUT_READ        = (byte)0x9A;
		byte INPUT_TEST        = (byte)0x9B;
		byte INPUT_READY       = (byte)0x9C;
		byte INPUT_READSI      = (byte)0x9D;
		byte INPUT_READEXT     = (byte)0x9E;
		byte INPUT_WRITE       = (byte)0x9F;

		byte OUTPUT_SET_TYPE   = (byte)0xA1;
		byte OUTPUT_RESET      = (byte)0xA2;
		byte OUTPUT_STOP       = (byte)0xA3;
		byte OUTPUT_POWER      = (byte)0xA4;
		byte OUTPUT_SPEED      = (byte)0xA5;
		byte OUTPUT_START      = (byte)0xA6;
		byte OUTPUT_POLARITY   = (byte)0xA7;
		byte OUTPUT_READ       = (byte)0xA8;
		byte OUTPUT_TEST       = (byte)0xA9;
		byte OUTPUT_READY      = (byte)0xAA;

		byte OUTPUT_STEP_POWER = (byte)0xAC;
		byte OUTPUT_TIME_POWER = (byte)0xAD;
		byte OUTPUT_STEP_SPEED = (byte)0xAE;
		byte OUTPUT_TIME_SPEED = (byte)0xAF;
		byte OUTPUT_STEP_SYNC  = (byte)0xB0;
		byte OUTPUT_TIME_SYNC  = (byte)0xB1;
		byte OUTPUT_CLR_COUNT  = (byte)0xB2;
		byte OUTPUT_GET_COUNT  = (byte)0xB3;
		byte OUTPUT_PRG_STOP   = (byte)0xB4;












		byte FILE              = (byte)0xC0; 
		byte ARRAY             = (byte)0xC1; 
		byte ARRAY_WRITE       = (byte)0xC2; 
		byte ARRAY_READ        = (byte)0xC3; 
		byte ARRAY_APPEND      = (byte)0xC4; 
		byte MEMORY_USAGE      = (byte)0xC5; 
		byte FILENAME          = (byte)0xC6; 

		byte READ8             = (byte)0xC8;
		byte READ16            = (byte)0xC9;
		byte READ32            = (byte)0xCA;
		byte READF             = (byte)0xCB;
		byte WRITE8            = (byte)0xCC;
		byte WRITE16           = (byte)0xCD;
		byte WRITE32           = (byte)0xCE;
		byte WRITEF            = (byte)0xCF;
		byte COM_READY         = (byte)0xD0; 
		byte COM_TEST          = (byte)0xD1; 

		byte COM_GET           = (byte)0xD3; 
		byte COM_SET           = (byte)0xD4; 



		byte MAILBOX_OPEN      = (byte)0xD8; 
		byte MAILBOX_WRITE     = (byte)0xD9; 
		byte MAILBOX_READ      = (byte)0xDA; 
		byte MAILBOX_TEST      = (byte)0xDB; 
		byte MAILBOX_READY     = (byte)0xDC; 
		byte MAILBOX_CLOSE     = (byte)0xDD; 	
	}	
	public interface COM_SET {
		final byte SET_BRICKNAME = (byte) 0x08;
	}


	public interface ColorSensorMode {
		byte REFLECTION = SensorMode.MODE0; 
		byte AMBIENT    = SensorMode.MODE1;
		byte COLOR      = SensorMode.MODE2;
		byte RAW        = SensorMode.MODE3;
		byte NXT_GREEN  = SensorMode.MODE3;
		byte NXT_BLUE   = SensorMode.MODE4;

	}
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
	public interface MotorPort {
		byte OUT_A = (byte)0x00;
		byte OUT_B = (byte)0x01;
		byte OUT_C = (byte)0x02;
		byte OUT_D = (byte)0x03;
	}
	public interface OutputBitfield {
		byte OUT_A = (byte)0b00000001;
		byte OUT_B = (byte)0b00000010;
		byte OUT_C = (byte)0b00000100;
		byte OUT_D = (byte)0b00001000;
	}
	public interface Polarity {
		int BACKWARD = -1;
		int FORWARD  =  1;
		int OPPOSITE_DIRECTION = 0;
	}
	public interface SensorMode {
		byte MODE0 = (byte)0x00; 
		byte MODE1 = (byte)0x01; 
		byte MODE2 = (byte)0x02; 
		byte MODE3 = (byte)0x03; 
		byte MODE4 = (byte)0x04; 
		byte MODE5 = (byte)0x05; 
		byte MODE6 = (byte)0x06;
	}
	public interface SensorPort {
		byte IN1 = (byte)0x00;
		byte IN2 = (byte)0x01;
		byte IN3 = (byte)0x02;
		byte IN4 = (byte)0x03;
	}	

	/**
	 * System Command
	 * Byte 0 – 1: Reply size, Little Endian. Reply size not including these 2 bytes
	 * Byte 2 – 3: Message counter, Little Endian. Equals the Direct Command
	 * Byte 4: Reply type. See defines above
	 * Byte 5: System Command which this is reply to.
	 * Byte 6: System Reply Status – Error, info or success. See the definitions below:
	 * Byte 7 – n: Further System
	 * System Reply Bytes:
	 * Byte 0 – 1: Reply size, Little Endian. Reply size not including these 2 bytes
	 * Byte 2 – 3: Message counter, Little Endian. Equals the Direct Command
	 * Byte 4: Reply type. See defines above
	 * Byte 5: System Command which this is reply to.
	 * Byte 6: System Reply Status – Error, info or success. See the definitions below:
	 * Byte 7 – n: Further System
	 */ 
	public interface SystemCommand {
		byte SYSTEM_COMMAND_REPLY    = (byte)0x01; // System command, reply required
		byte SYSTEM_COMMAND_NO_REPLY = (byte)0x81; // System command, reply not require
		//System Commands:
		byte BEGIN_DOWNLOAD      = (byte)0x92; // Begin file download
		byte CONTINUE_DOWNLOAD   = (byte)0x93; // Continue file download
		byte BEGIN_UPLOAD        = (byte)0x94; // Begin file upload
		byte CONTINUE_UPLOAD     = (byte)0x95; // Continue file upload
		byte BEGIN_GETFILE       = (byte)0x96; // Begin get bytes from a file (while writing to the file)
		byte CONTINUE_GETFILE    = (byte)0x97; // Continue get byte from a file (while writing to the file)
		byte CLOSE_FILEHANDLE    = (byte)0x98; // Close file handle
		byte LIST_FILES          = (byte)0x99; // List files
		byte CONTINUE_LIST_FILES = (byte)0x9A; // Continue list files
		byte CREATE_DIR          = (byte)0x9B; // Create directory
		byte DELETE_FILE         = (byte)0x9C; // Delete
		byte LIST_OPEN_HANDLES   = (byte)0x9D; // List handles
		byte WRITEMAILBOX        = (byte)0x9E; // Write to mailbox
		byte BLUETOOTHPIN        = (byte)0x9F; // Transfer trusted pin code to brick
		byte ENTERFWUPDATE       = (byte)0xA0; // Restart the brick in Firmware update mode	
		//System command replies
		byte SYSTEM_REPLY        = (byte)0x03; // System command reply OK
		byte SYSTEM_REPLY_ERROR  = (byte)0x05; // System command reply ERROR
		//SYSTEM command Reply Status codes:
		byte SUCCESS              = (byte)0x00;
		byte UNKNOWN_HANDLE       = (byte)0x01;
		byte HANDLE_NOT_READY     = (byte)0x02;
		byte CORRUPT_FILE         = (byte)0x03;
		byte NO_HANDLES_AVAILABLE = (byte)0x04;
		byte NO_PERMISSION        = (byte)0x05;
		byte ILLEGAL_PATH         = (byte)0x06;
		byte FILE_EXITS           = (byte)0x07;
		byte END_OF_FILE          = (byte)0x08;
		byte SIZE_ERROR           = (byte)0x09;
		byte UNKNOWN_ERROR        = (byte)0x0A;
		byte ILLEGAL_FILENAME     = (byte)0x0B;
		byte ILLEGAL_CONNECTION   = (byte)0x0C;
	}
	interface SoundCom {
		byte BREAK  = (byte)0x00;
		byte TONE   = (byte)0x01;
		byte PLAY   = (byte)0x02;
		byte REPEAT = (byte)0x03;
	}
	
	byte DIRECT_COMMAND_REPLY    = (byte)0x00;
	byte DIRECT_COMMAND_NO_REPLY = (byte)0x80;    
    byte DIRECT_REPLY_OK         = (byte) 0x02;
    byte DIRECT_REPLY_ERROR      = (byte) 0x04;
}
