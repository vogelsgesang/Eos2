package de.lathanda.eos.common.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.Utilities;

import de.lathanda.eos.base.ResourceLoader;
import de.lathanda.eos.common.gui.GuiConfiguration.GuiConfigurationListener;
/**
 * Diese Komponente zeigt zusätzliche Informationen zum Editorfeld an.
 *
 * @author Peter (Lathanda) Schneider
 */
public class SideInformation extends JPanel implements DocumentListener, GuiConfigurationListener, MouseListener {
	private static final long serialVersionUID = 4837891668872540779L;
	private static final BufferedImage BREAKPOINT_IMAGE = ResourceLoader
			.loadImage("icons/pictograms-road_signs-stop_sign.png");
	private static final BufferedImage ERROR_IMAGE = ResourceLoader.loadImage("icons/dialog-error-3.png");
	private SourceCode sourceCode;
	private JTextComponent component;

	private int descent;
	private FontMetrics fontMetrics;
	private Font font;
	private int lines;
	private int width;
	public SideInformation(JTextComponent component, SourceCode sourceCode) {
		this.component = component;
		this.sourceCode = sourceCode;
		setBorder(new CompoundBorder(new MatteBorder(0, 0, 0, 2, Color.GRAY), new EmptyBorder(0, 5, 0, 5)));
		sourceCode.addDocumentListener(this);
		sourceCode.setSideInformation(this);
		addMouseListener(this);
		setFont(new Font("Serif", Font.PLAIN, GuiConfiguration.def.getFontsize()));
		updateSize();
	}
	private void updateSize() {		
		int digits = Math.max(String.valueOf(lines).length(), 3);
		Insets insets = getInsets();
		setWidth(insets.left + insets.right + fontMetrics.charWidth('0') * digits);
	}
	private void setWidth(int width) {
		if (this.width != width) {
			this.width = width;
			this.invalidate();
		}
	}
	@Override
	public void setFont(Font font) {
		super.setFont(font);
		this.font = font;
		fontMetrics = getFontMetrics(font);
		descent = fontMetrics.getDescent();
		updateSize();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Rectangle clip = g.getClipBounds();
		Insets insets = getInsets();
		int rightBorder = getWidth() - insets.right;
		int position = component.viewToModel(new Point(0, clip.y));
		int endPosition = component.viewToModel(new Point(0, clip.y + clip.height));
		int lineNumber = 1;
		int center = getWidth() / 2;
		Element root = sourceCode.getDefaultRootElement();
		while (position <= endPosition) {
			try {
				lineNumber = root.getElementIndex(position) + 1;
				Rectangle r = component.modelToView(position);
				if (sourceCode.hasBreakpoint(lineNumber)) {
					g.drawImage(BREAKPOINT_IMAGE, center - r.height/2, r.y, r.height, r.height, null);
				} else if (sourceCode.hasError(lineNumber)) {
					g.drawImage(ERROR_IMAGE, center - r.height/2, r.y, r.height, r.height, null);
				} else {
					g.setFont(font);
					int stringWidth = fontMetrics.stringWidth(String.valueOf(lineNumber));
					int x = rightBorder - stringWidth;
					int y = r.y + r.height - descent;
					g.drawString(String.valueOf(lineNumber), x, y);
				}
				position = Utilities.getRowEnd(component, position) + 1;
			} catch (BadLocationException e) {
				break; //or endless loop would occure
			}
		}
		lines = lineNumber;
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		repaint();
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		repaint();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		repaint();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(width, 1000000);
	}

	@Override
	public void fontsizeChanged(int fontsize) {
		setFont(font.deriveFont((float)fontsize));
	}
	@Override
	public void mouseClicked(MouseEvent me) {
		int position = component.viewToModel(new Point(0, me.getY()));
		sourceCode.setToggleBreakpoint(position);
		repaint();
	}
	@Override
	public void mouseEntered(MouseEvent me) {	}
	@Override
	public void mouseExited(MouseEvent me) {	}
	@Override
	public void mousePressed(MouseEvent me) {	}
	@Override
	public void mouseReleased(MouseEvent me) { }
}