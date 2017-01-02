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
import java.awt.print.Paper;
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
    private final ArrayList<LinkedList<PrintPart>> pages = new ArrayList<>();
//******** fonts ********
    private final Font fontLiteral = new Font("monospaced", Font.PLAIN, 12);
    private final Font fontComment = fontLiteral.deriveFont(Font.ITALIC);
    private final Font fontKeyword = fontLiteral.deriveFont(Font.BOLD);
    private final Font fontLinenumber = fontLiteral;
    private final Font fontHeader = new Font("sans", Font.PLAIN, 10);
    private final Font fontFooter = new Font("sans", Font.PLAIN, 8);
    private static final double PAPER_BORDER_X = 56.69; //2cm in 1/72 inches
    private static final double PAPER_BORDER_Y = 28.34; //1cm in 1/72 inches
    private String header = "";

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

    	Paper paper = pageFormat.getPaper();
    	paper.setImageableArea(PAPER_BORDER_X, PAPER_BORDER_Y, paper.getWidth()-2*PAPER_BORDER_X, paper.getHeight()-2*PAPER_BORDER_Y);
    	pageFormat.setPaper(paper);
  	
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
	public void setHeader(String header) {
		this.header = header;
		parse(getGraphics());
	}
    void init(AbstractProgram abstractProgram, String header) {
        this.program = abstractProgram;
        this.header = header;
        parse(getGraphics());
    }

    private void parse(Graphics g) {
        LinkedList<PrintPart> page = new LinkedList<>();
    	pages.clear();
        pages.add(page);

        int sourceIndex = 0; //sourcecode index
        int tokenIndexBegin; //token index
        int tokenIndexEnd; //token index
        double lineheight = g.getFontMetrics(fontLiteral).getHeight() * 1.0;
        double lineNrOffset = g.getFontMetrics(fontLinenumber).stringWidth("XX: ");
        double headerHeight = lineheight;
        double footerHeight = lineheight; 
        

        double x = 0; //x within actual page
        double y = g.getFontMetrics(fontLiteral).getHeight(); //y within actual page
        
        double linewidth = pageFormat.getImageableWidth() - lineNrOffset;
        double pageheight = pageFormat.getImageableHeight() - headerHeight - footerHeight;
        double offsetX = pageFormat.getImageableX() + lineNrOffset;
        double offsetY = pageFormat.getImageableY() + headerHeight;

        if (program == null) {
        	return;
        }
        
        String source = program.getSource();
        int linenumber = 0;        

        for (InfoToken st : program.getTokenList()) {
        	if (st.getFormat() == InfoToken.IGNORE) continue;
            String token = source.substring(sourceIndex, st.getBegin() + st.getLength());
            tokenIndexBegin = 0;

            boolean realline = true;
            while (tokenIndexBegin < token.length()) {
                Text text = null;
                String part;
                boolean newline;
                Rectangle2D r = null;
                tokenIndexEnd = token.indexOf('\n', tokenIndexBegin);
              	if (tokenIndexEnd != -1) {
                   	newline = true;
                   	if (realline) {
                   		linenumber++;
                   		Text linenr = new Text(linenumber+": ", fontLinenumber, Color.BLACK, true);
                   		Rectangle2D bounds = linenr.getMetrics(g);
                   		linenr.move(offsetX - bounds.getWidth(), offsetY + y);
                   		page.add(linenr);
                   	}
               	} else {
                   	tokenIndexEnd = token.length();
                   	newline = false;
               	}
              	int skip = 1;
              	boolean fitting = false;
              	do {
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
                	if (fitting || r.getWidth() >= linewidth) {
                		if(x + r.getWidth() > linewidth) {
                			fitting = true;
                		} else {
                			fitting = false;
                		}
                		skip = 0;
                		realline = false;
                	} else {
                		skip = 1;
                		realline = true;
                	}
                } while (fitting);
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
                    if (y > pageheight) {
                        y = g.getFontMetrics(fontLiteral).getHeight();
                        page = new LinkedList<>();
                        pages.add(page);
                    }
                }
            }
            sourceIndex = st.getBegin() + st.getLength();
        }
        //add linenumber for last line
   		linenumber++;
   		Text linenr = new Text(linenumber+": ", fontLinenumber, Color.BLACK, true);
   		Rectangle2D bounds = linenr.getMetrics(g);
   		linenr.move(offsetX - bounds.getWidth(), offsetY + y);
   		page.add(linenr);
   		
   		//add headers
   		Text headerText = new Text(header, fontHeader, Color.BLACK, false);
   		headerText.move(offsetX + (linewidth - headerText.getMetrics(g).getWidth())/2, offsetY - 1);
   		Line headerLine = new Line(Color.BLACK, offsetX, offsetY, offsetX + linewidth, offsetY);
   		Line footerLine = new Line (Color.BLACK, offsetX, offsetY + pageheight, offsetX + linewidth, offsetY + pageheight);
        int pageNr = 1;
   		for (LinkedList<PrintPart> p:pages) {
        	Text footerText = new Text(pageNr+"/"+pages.size(), fontFooter, Color.BLACK, false);
        	footerText.move(offsetX + (linewidth - footerText.getMetrics(g).getWidth())/2, offsetY + pageheight + footerHeight);
        	pageNr++;
        	p.add(headerLine);
        	p.add(footerLine);
        	p.add(footerText);
        	p.add(headerText);
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
    private static interface PrintPart {
    	 public void print(Graphics g);
    }
    private class Text implements PrintPart {

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

        public void move(double x, double y) {
            this.x = (int)x;
            this.y = (int)y;
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
    private static class Line implements PrintPart {
        private final Color color;
        private int x1;
        private int y1;
        private int x2;
        private int y2;
		public Line(Color color, double x1, double y1, double x2, double y2) {
			this.color = color;
			this.x1 = (int)x1;
			this.y1 = (int)y1;
			this.x2 = (int)x2;
			this.y2 = (int)y2;
		}
        public void print(Graphics g) {
        	g.setColor(color);
        	g.drawLine(x1, y1, x2, y2);
        }
    }
}
