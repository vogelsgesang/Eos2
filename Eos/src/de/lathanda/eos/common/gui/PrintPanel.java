package de.lathanda.eos.common.gui;

import static de.lathanda.eos.common.gui.GuiConstants.GUI;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.JOptionPane;

import de.lathanda.eos.common.interpreter.AbstractProgram;
import de.lathanda.eos.common.interpreter.InfoToken;

/**
 * Druckvorschau.
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class PrintPanel extends javax.swing.JPanel implements Printable, Pageable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6226144081255555607L;
	private PageFormat pageFormat;
    private int pageIndex = 0;
    private boolean linenumbers = false;
    private AbstractProgram program;
//******** layout *******
    private final ArrayList<LinkedList<Text>> pages = new ArrayList<>();
//******** fonts ********
    private final Font fontLiteral = new Font("monospaced", Font.PLAIN, 12);
    private final Font fontComment = fontLiteral.deriveFont(Font.ITALIC);
    private final Font fontKeyword = fontLiteral.deriveFont(Font.BOLD);
    private final Font fontLinenumber = fontLiteral;

    /**
     * Creates new form PrintPanel
     */
    public PrintPanel() {
    	try {
    		if (PrinterJob.lookupPrintServices().length > 0) {
    			PrinterJob job = PrinterJob.getPrinterJob();
    			pageFormat = job.defaultPage();
    		} else {
    			pageFormat = new PageFormat();
    		}
    	} catch (Throwable t) {
            JOptionPane.showMessageDialog(this, GUI.getString("Print.Error.Title"),
                    t.getLocalizedMessage(),
                    JOptionPane.ERROR_MESSAGE
            );    		
    		pageFormat = new PageFormat();
    	}
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(400,300));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            print(g, pageFormat, pageIndex);
        } catch (PrinterException ex) {
            JOptionPane.showMessageDialog(this, GUI.getString("Print.Error.Title"),
                    ex.getLocalizedMessage(),
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        this.pageFormat = pageFormat;
        if (pageIndex < 0 || pageIndex >= pages.size()) {
            return NO_SUCH_PAGE;
        }

        pages.get(pageIndex).stream().forEachOrdered(t -> t.print(graphics));

        return PAGE_EXISTS;
    }

    void previousPage() {
        pageIndex = (pageIndex - 1 + pages.size()) % pages.size();
        repaint();
    }

    void nextPage() {
        pageIndex = (pageIndex + 1) % pages.size();
        repaint();
    }
	public void setLinenumberVisible(boolean visible) {
		linenumbers = visible;	
		repaint();
	}
    void init(AbstractProgram abstractProgram) {
        this.program = abstractProgram;
        parse(getGraphics());
    }

    private void parse(Graphics g) {
        LinkedList<Text> page = new LinkedList<>();
        pages.add(page);

        int sourceIndex = 0; //sourcecode index
        int tokenIndexBegin; //token index
        int tokenIndexEnd; //token index
        int lineNrOffset = g.getFontMetrics(fontLinenumber).stringWidth("XX: ");
        int offsetX = (int)pageFormat.getImageableX() + lineNrOffset;
        int offsetY = (int)pageFormat.getImageableY();
        int x = 0; //x within actual page
        int y = g.getFontMetrics(fontLiteral).getHeight(); //y within actual page
        
        double lineheight = g.getFontMetrics(fontLiteral).getHeight() * 1.0;
        double linewidth = pageFormat.getImageableWidth() - lineNrOffset;
        double pageheight = pageFormat.getImageableHeight();

        if (program == null) {
        	return;
        }
        
        String source = program.getSource();
        int linenumber = 1;
        boolean placeLinenumber = true;

        for (InfoToken st : program.getTokenList()) {
        	if (st.getFormat() == InfoToken.IGNORE) continue;
            String token = source.substring(sourceIndex, st.getBegin() + st.getLength());
            tokenIndexBegin = 0;
            while (tokenIndexBegin < token.length()) {
            	if (placeLinenumber) {
            		placeLinenumber = false;
            		Text linenr = new Text(linenumber+": ", fontLinenumber, Color.BLACK, true);
            		Rectangle2D bounds = linenr.getMetrics(g);
            		linenr.move((int)(offsetX + x - bounds.getWidth()), offsetY + y);
            		page.add(linenr);
            	}
                Text text = null;
                String part;
                boolean newline;
                Rectangle2D r = null;
                tokenIndexEnd = token.indexOf('\n', tokenIndexBegin);
              	if (tokenIndexEnd != -1) {
                   	newline = true;
               	} else {
                   	tokenIndexEnd = token.length();
                   	newline = false;
               	}
              	int skip = 1;
              	while (r == null || r.getWidth() >= linewidth) {
              		if (r != null) {
              			tokenIndexEnd--;
              			skip = 0;
              		}
                   	part = token.substring(tokenIndexBegin, tokenIndexEnd);
                	switch (st.getFormat()) {
                    	case InfoToken.COMMENT:
                        	text = new Text(part, fontComment, new Color(0, 160, 0), false);
                        	break;
                    	case InfoToken.LITERAL:
                        	text = new Text(part, fontLiteral, Color.BLACK, false);
                        	break;
                    	case InfoToken.KEYWORD:
                        	text = new Text(part, fontKeyword, Color.BLACK, false);
                        	break;
                    	default:
                        	text = new Text(part, fontLiteral, Color.BLACK, false);
                	}
                	r = text.getMetrics(g);
                }
            	tokenIndexBegin = tokenIndexEnd + skip;
                if (x + r.getWidth() > linewidth) {
                    x = 0;
                    y += lineheight;
                    if (y > pageheight) {
                        y = g.getFontMetrics(fontLiteral).getHeight();
                        page = new LinkedList<>();
                        pages.add(page);
                    }
                }
                text.move(offsetX + x, offsetY + y);
                x += r.getWidth();
                page.add(text);
                if (newline) {
                    x = 0;
                    y += lineheight;
                    linenumber++;
                    placeLinenumber = true;
                    if (y > pageheight) {
                        y = g.getFontMetrics(fontLiteral).getHeight();
                        page = new LinkedList<>();
                        pages.add(page);
                    }
                }
            }
            sourceIndex = st.getBegin() + st.getLength();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension((int)pageFormat.getWidth(), (int)pageFormat.getHeight());
    }

    @Override
    public int getNumberOfPages() {
        return pages.size();
    }

    @Override
    public PageFormat getPageFormat(int pageIndex) throws IndexOutOfBoundsException {
        return pageFormat;
    }

    @Override
    public Printable getPrintable(int pageIndex) throws IndexOutOfBoundsException {
        return this;
    }

    private class Text {

        private final String text;
        private final Font font;
        private final Color color;
        private int x;
        private int y;
        private boolean linenumber;
        public Text(String text, Font font, Color color, boolean linenumber) {
            this.text = text;
            this.font = font;
            this.color = color;
            this.linenumber = linenumber;
        }

        public void move(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public void print(Graphics g) {
        	if (!linenumber || linenumbers) {
        		g.setColor(color);
        		g.setFont(font);
        		g.drawString(text, x, y);
        	}
        }

        public Rectangle2D getMetrics(Graphics g) {
            FontMetrics fm = g.getFontMetrics(font);
            return fm.getStringBounds(text, g);
        }
    }

}
