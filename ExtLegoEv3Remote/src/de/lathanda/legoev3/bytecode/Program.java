package de.lathanda.legoev3.bytecode;

public class Program {
	/*
	 * sign 4 bytes
	 * image size 4 bytes
	 * version info 2 byte
	 * number of objects 2 bytes
	 * number of global bytes 4 bytes
	 */
	private byte[] imageHeader  = new byte[16];
	/*
	 * offset to instructions  4 bytes
	 * owner object id 2 bytes
	 * trigger count 2 bytes
	 * local bytes 4 bytes
	 */
	private byte[] objectHeader = new byte[12];
	private byte[] bytecode; 
}
