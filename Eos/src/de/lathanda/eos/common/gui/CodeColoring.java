package de.lathanda.eos.common.gui;

import java.awt.Color;
import java.util.LinkedList;

import javax.swing.SwingUtilities;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import de.lathanda.eos.common.interpreter.InfoToken;
import de.lathanda.eos.common.interpreter.Marker;
import de.lathanda.eos.util.GuiToolkit;

/**
 * Diese Klasse färbt ein Dokument abhängig vom Parser.
 *  
 * @author Peter (Lathanda) Schneider
 *
 */
public class CodeColoring implements CodeColorHook {
	/**
	 * Basisformatierung.
	 */
	private final SimpleAttributeSet attributeSetBase = new SimpleAttributeSet();
	/**
	 * Markiert (Aktive Codezeile).
	 */
	private final SimpleAttributeSet attributeSetMark = new SimpleAttributeSet();
	/**
	 * Unmarkiert.
	 */
	private final SimpleAttributeSet attributeSetUnmark = new SimpleAttributeSet();
	/**
	 * Fehlerhafter Quellcode.
	 */
	private final SimpleAttributeSet attributeSetError = new SimpleAttributeSet();
	/**
	 * Kommentar.
	 */
	private final SimpleAttributeSet attributeSetComment = new SimpleAttributeSet();
	/**
	 * Konstante.
	 */
	private final SimpleAttributeSet attributeSetLiteral = new SimpleAttributeSet();
	/**
	 * Schlüsselwort.
	 */
	private final SimpleAttributeSet attributeSetKeyword = new SimpleAttributeSet();
	/**
	 * Quellcode Dokument.
	 */
	private SourceCode sourceCode;
	/**
	 * Neue Codemarkierung. Die Markierung erfolgt verzögert.
	 */
	private Marker newCodePointer;
	/**
	 * Aktuelle Codemarkierung.
	 */
	private Marker codePointer;
	/**
	 * Codemarkierung muss aktuallisiert werden.
	 */
	private boolean codePointerDirty = false;

	/**
	 * Erzeugt ein Quellcodeformatierungsobjekt
	 */
	public CodeColoring() {
		this.codePointer = new Marker();
		StyleConstants.setBackground(attributeSetBase, Color.WHITE);
		StyleConstants.setForeground(attributeSetBase, Color.BLACK);
		StyleConstants.setUnderline(attributeSetBase, false);
		StyleConstants.setBold(attributeSetBase, false);
		StyleConstants.setForeground(attributeSetComment, new Color(0, 160, 0));
		StyleConstants.setForeground(attributeSetLiteral, Color.BLUE);
		StyleConstants.setForeground(attributeSetKeyword, Color.BLACK);
		StyleConstants.setForeground(attributeSetError, Color.RED);
		StyleConstants.setBold(attributeSetKeyword, true);
		StyleConstants.setBackground(attributeSetMark, Color.YELLOW);
		StyleConstants.setBackground(attributeSetUnmark, Color.WHITE);
	}

	/**
	 * Schriftgröße ändern
	 * @param size
	 */
	public void setFontSize(int size) {
		StyleConstants.setFontSize(attributeSetBase,    GuiToolkit.getFontSize(size));
		StyleConstants.setFontSize(attributeSetComment, GuiToolkit.getFontSize(size));
		StyleConstants.setFontSize(attributeSetLiteral, GuiToolkit.getFontSize(size));
		StyleConstants.setFontSize(attributeSetKeyword, GuiToolkit.getFontSize(size));
		StyleConstants.setFontSize(attributeSetMark,    GuiToolkit.getFontSize(size));
		StyleConstants.setFontSize(attributeSetUnmark,  GuiToolkit.getFontSize(size));
	}

	/**
	 * Formatiert den Text neu.
	 */
	public void doColoring() {
		if (sourceCode.getProgram() == null) {
			sourceCode.setCharacterAttributes(0, sourceCode.getLength(), attributeSetBase, true);
			return;
		}
		LinkedList<InfoToken> tokens = sourceCode.getProgram().getTokenList();
		// remove previous coloring
		// change size
		sourceCode.setCharacterAttributes(0, sourceCode.getLength(), attributeSetBase, true);
		for (InfoToken t : tokens) {
			switch (t.getFormat()) {
			case InfoToken.COMMENT:
				sourceCode.setCharacterAttributes(t.getBegin(), t.getLength(), attributeSetComment, true);
				break;
			case InfoToken.LITERAL:
				sourceCode.setCharacterAttributes(t.getBegin(), t.getLength(), attributeSetLiteral, true);
				break;
			case InfoToken.KEYWORD:
				sourceCode.setCharacterAttributes(t.getBegin(), t.getLength(), attributeSetKeyword, true);
				break;
			case InfoToken.PLAIN:
				// no formating
			default:
				// no formating
			}
		}
	}

	/**
	 * Markiert den gerade ausgeführten Quellcode.
	 */
	private synchronized void markExecutionPoint() {
		if (codePointer != null) {
			sourceCode.setCharacterAttributes(codePointer.getBeginPosition(),
					codePointer.getLength(), attributeSetUnmark, false);
		}
		codePointer = newCodePointer;
		codePointerDirty = false;

		if (codePointer != null) {
			sourceCode.setCharacterAttributes(codePointer.getBeginPosition(), codePointer.getLength(), attributeSetMark,
					false);
		}
	}

	/**
	 * Entfernt die Markierung vom aktuell markierten Quellcode
	 */
	public synchronized void unmarkExecutionPoint() {
		if (codePointer != null) {
			int begin = codePointer.getBeginPosition();
			int length = codePointer.getLength();
			sourceCode.setCharacterAttributes(begin, length, attributeSetUnmark, false);
		}
	}

	/**
	 * Markiert einen Fehler.
	 * @param code Bereich der fehlerhaft ist.
	 */
	public void markError(Marker code) {
		sourceCode.setCharacterAttributes(code.getBeginPosition(), code.getLength(), attributeSetError, false);
	}

	/**
	 * Fordert die Markierung des Quellcodebereichs
	 * als aktuell ausgeführt an. Die Markierung erfolgt verzögert.
	 * Bei hoher Frequenz kann es vorkommen, dass nicht alle Markierungen tatsächlich
	 * ausgeführt werden.
	 * @param codeRange Bereich der markiert werden soll.
	 */
	public synchronized void markExecutionPoint(Marker codeRange) {
		newCodePointer = codeRange;
		if (!codePointerDirty) {
			codePointerDirty = true;
			SwingUtilities.invokeLater(() -> markExecutionPoint());
		}
	}

	@Override
	public void init(SourceCode source) {
		this.sourceCode = source;
	}
}
