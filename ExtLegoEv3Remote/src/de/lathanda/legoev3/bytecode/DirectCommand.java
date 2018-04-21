package de.lathanda.legoev3.bytecode;
/**
 * Direct Command Bytes:
 * Byte 0 – 1: Command size, Little Endian. Command size not including these 2 bytes
 * Byte 2 – 3: Message counter, Little Endian. Forth running counter
 * Byte 4: Command type. See defines above
 * Byte 5 - 6: Reservation (allocation) of global and local variables using a compressed format (globals reserved in byte 5 and the 2 lsb of byte 6, locals reserved in the upper 6 bits of byte 6) – see below:
 * Byte 7 – n: Byte codes as a single command or compound commands (I.e. more commands composed as a small program)
 * Locals = “l” and Globals = “g”
 * Direct Reply Bytes:
 * Byte 0 – 1: Reply size, Little Endian. Reply size not including these 2 bytes
 * Byte 2 – 3: Message counter, Little Endian. Equals the Direct Command
 * Byte 4: Reply type. See defines above
 * Byte 5 - n: Resonse buffer. I.e. the content of the by the Command reserved global variables. I.e. if the command reserved
 * 
 * LCS Long variable type Length bytes STRING zero terminated
 * LC0(v) Short constant(value) single byte +/- 31
 * LC1(v) Long constant(value) one byte to follow (2 bytes) +/- 127
 * LC2(v) Long constant(value) two bytes to follow (3 bytes) +/- 32767
 * LC4(v) Long constant(value) four bytes to follow (5 bytes) +/- 2147483647
 * LV0(i) Short LOCAL variable(adr) single byte at adr +/- 31
 * LV1(i) Long LOCAL variable(adr) one byte to follow at adr (2 bytes) +/- 127
 * LV2(i) Long LOCAL variable(adr) two bytes to follow at adr (3 bytes) +/- 32767
 * LV4(i) Long LOCAL variable(adr) four bytes to follow at adr (5 bytes) +/- 2147483647
 * GV0(i) Short GLOBAL variable(adr) single byte at adr +/- 31
 * GV0(i) Long GLOBAL variable(adr) one byte to follow at adr (2 bytes) +/- 127
 * GV0(i) Long GLOBAL variable(adr) two bytes to follow at adr (3 bytes) +/- 32767
 * GV0(i) Long GLOBAL variable(adr) four bytes to follow at adr (5 bytes) +/- 2147483647
 */
public interface DirectCommand {
	//Direct Commands
	byte DIRECT_COMMAND_REPLY    = (byte)0x00; // Direct command, reply required
	byte DIRECT_COMMAND_NO_REPLY = (byte)0x80; // Direct command, reply not require	
	//Direct Replies
	byte DIRECT_REPLY            = (byte)0x02; // Direct command reply OK
	byte DIRECT_REPLY_ERROR      = (byte)0x04; // Direct command reply ERROR
}
