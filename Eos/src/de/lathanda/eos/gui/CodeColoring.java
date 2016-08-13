package de.lathanda.eos.gui;

import java.awt.Color;
import java.util.LinkedList;

import javax.swing.SwingUtilities;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import de.lathanda.eos.common.CodeColorHook;
import de.lathanda.eos.common.Marker;
import de.lathanda.eos.common.SourceCode;
import de.lathanda.eos.interpreter.javacc.SourceToken;

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
	 * @param sourceCode Der zugeordnete Quellcode
	 */
	public CodeColoring() {
		this.codePointer = new Marker();
		StyleConstants.setBackground(attributeSetBase, Color.WHITE);
		StyleConstants.setForeground(attributeSetBase, Color.BLACK);
		StyleConstants.setUnderline(attributeSetBase, false);
		StyleConstants.setBold(attributeSetBase, false);
		StyleConstants.setForeground(attributeSetComment, Color.GREEN);
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
		StyleConstants.setFontSize(attributeSetBase, size);
		StyleConstants.setFontSize(attributeSetComment, size);
		StyleConstants.setFontSize(attributeSetLiteral, size);
		StyleConstants.setFontSize(attributeSetKeyword, size);
		StyleConstants.setFontSize(attributeSetMark, size);
		StyleConstants.setFontSize(attributeSetUnmark, size);
	}

	/**
	 * Formatiert den Text neu.
	 */
	public void doColoring() {
		SwingUtilities.invokeLater(() -> doSwingColoring());
	}

	private void doSwingColoring() {
		LinkedList<SourceToken> tokens = sourceCode.getProgram().getTokenList();
		// remove previous coloring, +1 is needed or the last symbol will not
		// change size
		sourceCode.setCharacterAttributes(0, sourceCode.getLength() + 1, attributeSetBase, false);
		for (SourceToken t : tokens) {
			switch (t.getFormat()) {
			case COMMENT:
				sourceCode.setCharacterAttributes(t.getBegin(), t.getLength(), attributeSetComment, false);
				break;
			case LITERAL:
				sourceCode.setCharacterAttributes(t.getBegin(), t.getLength(), attributeSetLiteral, false);
				break;
			case KEYWORD:
				sourceCode.setCharacterAttributes(t.getBegin(), t.getLength(), attributeSetKeyword, false);
				break;
			case PLAIN:
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
			SwingUtilities.invokeLater(() -> sourceCode.setCharacterAttributes(begin, length
					, attributeSetUnmark, false));
		}
	}

	/**
	 * Markiert einen Fehler.
	 * @param code Bereich der fehlerhaft ist.
	 */
	public void markError(Marker code) {
		SwingUtilities.invokeLater(() -> sourceCode.setCharacterAttributes(code.getBeginPosition(), code.getLength(),
				attributeSetError, false));
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
