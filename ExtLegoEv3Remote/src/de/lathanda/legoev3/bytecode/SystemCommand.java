package de.lathanda.legoev3.bytecode;
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
