package de.lathanda.eos.gui.diagram;

public class TextUnit extends Unit {
	private String text;
	private float x;
	private float y;
	private float textheight;
	private float textwidth;
	private float ascent;
	public TextUnit(String text) {
		super();
		if (text != null) {
			this.text = text;
		} else {
			this.text = "\uFFFD";
		}
	}
	@Override
	public void drawUnit(Drawing d) {
		d.drawString(text, x, y);
	}
	@Override
	public void layoutUnit(Drawing d) {
		textheight = d.getHeight();
		textwidth = d.stringWidth(text);
		ascent = d.getAscent();
		height = textheight;
		width = textwidth;
		y = ascent;
		x = 0;
	} 
	public void alignCentralX() {
		x = (width - textwidth) / 2;
	}
	public void alignLeft() {
		x = 0;		
	}
	public void alignRight() {
		x = width - textwidth;
	}
	public void allignCentralY() {
		y = (height - textheight) / 2 + ascent;		
	}
	public void alignTop() {
		y = ascent;
	}
	public void alignBottom() {
		y = height - textheight + ascent;
	}
}
