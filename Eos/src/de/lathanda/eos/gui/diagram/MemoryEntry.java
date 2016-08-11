package de.lathanda.eos.gui.diagram;

public class MemoryEntry implements Comparable<MemoryEntry> {
	public final String name;
	public final Object data;
	public final String type;
	
	public MemoryEntry(String name, Object data, String type) {
		super();
		this.name = name;
		this.data = data;
		this.type = type;
	}

	@Override
	public int compareTo(MemoryEntry b) {
		return name.compareTo(b.name);
	}

	@Override
	public boolean equals(Object obj) {
		if ( obj instanceof MemoryEntry) {
			return name.equals(((MemoryEntry)obj).name);
		} else {
			return false;
		}
	}
	
}
