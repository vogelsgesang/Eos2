package de.lathanda.eos.common.gui;

import java.awt.Image;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.rtf.RTFEditorKit;

/**
 * \brief Zwischenablage
 * 
 * Eine Sammlung von Methoden zur Ansteuerung der Zwischenablage.
 * 
 * Die Methoden arbeiten leider nur mit Einschränkungen,
 * 
 * 
 *
 * @author Peter (Lathanda) Schneider
 */
public class ClipBoard {
	/**
	 * Einfacher Text
	 */
    private static DataFlavor PLAIN = new DataFlavor(String.class , "text/plain");
    /**
     * Html
     */
    private static DataFlavor HTML  = new DataFlavor(ByteArrayInputStream.class, "text/html");
    /**
     * Richtext
     */
    private static DataFlavor RTF   = new DataFlavor(ByteArrayInputStream.class, "text/rtf");
    /**
     * Ein Bild in die Zwischenablage legen.
     * @param image Bild
     */
    public static void pushImage(Image image) {
        TransferableImage timage = new TransferableImage(image);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(timage, null);
    }
    /**
     * Ein Dokument in die Zwischenablage legen.
     * @param doc Dokument
     */
    public static void pushDocument(Document doc) {
        TransferableDocument tdoc = new TransferableDocument(doc);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(tdoc, null);
    }
    /**
     * Einen Teil eines Dokumentes in die Zwischenablage legen.
     * @param doc Dokument
     * @param begin Anfang
     * @param length Länge
     */
    public static void pushDocument(Document doc, int begin, int length) {
        TransferableDocument tdoc = new TransferableDocument(doc, begin, length);
        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(tdoc, null);
    }
    /**
     * Liest einen Text aus der Zwischenablage.
     * @return Der Text.
     */
    public static String pollText() {
    	try {
    		Transferable t = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(PLAIN);
    		return (String)t.getTransferData(PLAIN);
        } catch (Throwable t) {
            //Possible errors
        	//IllegalStateException, platform has no clipboard
        	//NullPointerException, clipboard is empty
        	//UnsupportedFlavorException, clipboard contains no text
        	//we don't really care why it failed, we return always an empty string
            return "";
        }
    }
    /**
     * Text-Transferklasse für die Zwischenablage.
     * @author Peter (Lathanda) Schneider
     *
     */
    private static class TransferableDocument implements Transferable {
    	/**
    	 * Textrepräsentation.
    	 */
        String text;
        /**
         * Richtextrepräsentation
         */
        ByteArrayInputStream rtf;
        /**
         * Htmlrepräsentation.
         */
        ByteArrayInputStream html;
        /**
         * Erzeugt ein Transferobjekt für das ganze Dokument.
         * @param doc Dokument
         */
        public TransferableDocument(Document doc) {
            this(doc, 0, doc.getLength());
        }
        /**
         * Erzeugt ein Transferobjekt für einen Teil des Dokuments.
         * @param doc Dokument
         * @param begin Anfang
         * @param length Länge
         */
        public TransferableDocument(Document doc, int begin, int length) {
            //create fallback text
            try {
                text = doc.getText(begin, length);
            } catch (BadLocationException ex) {
                //this should never happen
            }
            //create rtf
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                RTFEditorKit rek = new RTFEditorKit();
                rek.write(baos, doc, begin, length); //TODO, Bug in Java API
                rtf = new ByteArrayInputStream(baos.toByteArray());
            } catch (BadLocationException | IOException ex) {
                //this should never happen
            }
            //create html
            try {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                HTMLEditorKit hek = new HTMLEditorKit();
                hek.write(baos, doc, begin, length); //TODO, Bug in Java API
                html = new ByteArrayInputStream(baos.toByteArray());
            } catch (BadLocationException | IOException ex) {
                //this should never happen
            }
            
        }
        
        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[]{PLAIN,RTF,HTML};
        }

        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return flavor.equals(PLAIN) ||
                flavor.equals(RTF) ||
                flavor.equals(HTML);
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            if (flavor.equals(PLAIN)){
                return text;
            } else if (flavor.equals(RTF)){
                return rtf;
            } else if (flavor.equals(HTML)) {
                return html;
            } else{
                throw new UnsupportedFlavorException(flavor);
            }
        }
    }
    /**
     * Bild-Transferklasse für die Zwischenablage.
     * @author Peter (Lathanda) Schneider
     *
     */
    private static class TransferableImage implements Transferable {
    	/**
    	 * Bilddaten
    	 */
        private final Image image;
        /**
         * Erzeugt ein Bild-Transferobjekt.
         * @param image Bild
         */
        public TransferableImage(Image image) {
            this.image = image;
        }
        @Override
        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { DataFlavor.imageFlavor };
        }
 
        @Override
        public boolean isDataFlavorSupported(DataFlavor flavor) {
            return DataFlavor.imageFlavor.equals(flavor);
        }

        @Override
        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException  {
            if (!DataFlavor.imageFlavor.equals(flavor)) {
                throw new UnsupportedFlavorException(flavor);
            }
            return image;
        }
    }
}  

