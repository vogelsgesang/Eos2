package de.lathanda.eos.gui;

import javax.swing.event.DocumentEvent;
import javax.swing.text.AbstractDocument;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;
/**
 * Dieser UndoManager ignoriert Färbeaktionen und informiert die Quelltextverwaltung
 * wenn sich der Sourcecode durch redo undo geändert hat.
 * @author Peter (Lathanda) Schneider
 *
 */
public class TextUndoManager extends UndoManager {

	private static final long serialVersionUID = 474701055889883986L;
	private SourceCode data;
	public TextUndoManager(SourceCode data) {
		this.data = data;
	}

	@Override
	public synchronized boolean addEdit(UndoableEdit e) {
		AbstractDocument.DefaultDocumentEvent event = (AbstractDocument.DefaultDocumentEvent)e;
		if  (event.getType().equals(DocumentEvent.EventType.CHANGE)) {
			return true;
		}
		return super.addEdit(e);
	}

	@Override
	public synchronized void redo() throws CannotRedoException {
		super.redo();
		data.changed();
	}

	@Override
	public synchronized void undo() throws CannotUndoException {
		super.undo();
		data.changed();
	}	
}
