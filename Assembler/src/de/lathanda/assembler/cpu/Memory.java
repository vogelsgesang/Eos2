package de.lathanda.assembler.cpu;

import java.util.TreeMap;

public class Memory {
	private TreeMap<Integer, Cell[]> mem;
	public Memory() {
		mem = new TreeMap<>();
	}
	private Cell[] getMemoryBlock(int addr) {
		Integer blockNumber = addr >> 10;
		if (mem.containsKey(blockNumber)) {
			return mem.get(blockNumber);
		} else {
			Cell[] block = new Cell[1<<10];
			mem.put(blockNumber,  block);
			return block;
		}
	}
	/**
	 * Lesender Speicherzugriff.
	 * @param addr
	 * @return
	 */
	public int readMemory(int addr) {
		Integer blockNumber = addr >> 10;
		if (mem.containsKey(blockNumber)) {
			Cell[] block = mem.get(blockNumber);
			Cell cell = block[addr & 0b1111111111]; 
			if (cell != null) {
				return cell.getInt();
			} else {
				return 0;
			}
		} else {
			return 0;
		}
	}
	public Cell getMemoryCell(int addr) {
		Cell[] block = getMemoryBlock(addr);
		Cell cell = block[addr & 0b1111111111];
		if (cell != null) {
			return cell;
		} else {
			cell = new Cell();
			block[addr & 0b1111111111] = cell;
			return cell;
		}
	}
	public Cell getMemoryCell(Cell c) {
		return getMemoryCell(c.getInt());
	}
	public Cell getMemoryCell(Register r) {
		return getMemoryCell(r.getInt());
	}
}
