package de.lathanda.eos.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JWindow;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.ToolTipManager;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.AbstractDocument;
import javax.swing.text.JTextComponent;
import de.lathanda.eos.common.interpreter.AbstractProgram;
import de.lathanda.eos.common.interpreter.AbstractType;
import de.lathanda.eos.common.interpreter.AutoCompleteHook;
import de.lathanda.eos.common.interpreter.AutoCompleteInformation;
import de.lathanda.eos.interpreter.Type;
import de.lathanda.eos.spi.AutoCompleteEntry;
import de.lathanda.eos.spi.LanguageManager;
/**
 * \brief Codevervollständigung
 * 
 * Verwaltet das Auswahlmenue für automatische Vervollständigung und Templates.
 *
 * @author Peter (Lathanda) Schneider
 */
public class AutoCompletion implements CaretListener, KeyListener, FocusListener, ComponentListener, AutoCompleteHook {
	private static LanguageManager lm = LanguageManager.getInstance();
	/**
	 * Letzte Cursorposition für Filterung, Abbruch, etc. 
	 */
	private int lastPosition = -1;
	/**
	 * Startposition für FIlterung, Abbruch, etc.
	 */
	private int startPosition = -1;
	/**
	 * Entscheidet ob der Dialog gerade aktiv ist.
	 */
	private boolean active = false;
	/**
	 * Auswahlfenster
	 */
	private JWindow choiceWindow;
	/**
	 * Mögliche Auswahloptionen.
	 */
	private TreeSet<AutoCompleteInformation> choiceItems = new TreeSet<>();
	/**
	 * Das Zeichen wurde bereits verwendet und darf nicht getippt werden.
	 */
	private boolean consumeNextKey = false;
	/**
	 * Auswahlliste
	 */
	private final TooltipList choiceList;
	/**
	 * Textkomponente in der die Auswahl angezeigt wird.
	 */
	private final JTextComponent component;
	public AutoCompletion(JTextComponent component, JFrame mainWindow) {
		this.component = component;
		choiceWindow = new JWindow();
		choiceWindow.setFocusable(false);
		choiceList = new TooltipList();
		choiceList.setCellRenderer(new ChoiceCellRenderer());
		choiceList.setEnabled(true);
		choiceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		choiceList.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e ) {
			    if ( e.getClickCount() == 2 ) {
			        complete();
			    }
			}
		});
		JScrollPane choiceScroll = new JScrollPane(choiceList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		choiceWindow.getContentPane().add(choiceScroll);
		component.addCaretListener(this);
		component.addKeyListener(this);
		component.addFocusListener(this);
		mainWindow.addComponentListener(this);
		component.addComponentListener(this);
	}
	
	/**
	 * Startet den Auswahldialog.
	 * @param base Typ für den die Auswahl generiert wird.
	 * @param position Position im Text für die die Auswahl gestart wurde.
	 * @throws BadLocationException
	 */
	public void start(AbstractType base, int position) throws BadLocationException {
		if (base.isUnknown()) {
			stop();
			return;
		}
		startPosition = position;
		lastPosition = position;
		for(AutoCompleteInformation aci:base.getAutoCompletes()) {
			if (aci.getType() != AutoCompleteInformation.PRIVATE) {
				choiceItems.add(aci);
			}
		}
		showMenue();
	}
	/**
	 * Hilfsmethode zeigt das Menue an.
	 */
	private void showMenue() {
		if (choiceItems.size() == 0) {
			stop();
			return;
		}
		updateChoices();
		setPosition();
		active = true;

		choiceWindow.setPreferredSize(new Dimension(200,200));
		Dimension listDimension = choiceList.getPreferredSize();
		choiceWindow.setSize(listDimension.width + 20, Math.min(listDimension.height + 10, 400));
		choiceWindow.setVisible(true);
		component.requestFocus();		
	}
	/**
	 * Startet die Templateauswahl. 
	 */
	public void startTemplate() {
		int position = component.getCaretPosition();
		startPosition = position;
		lastPosition = position;
		Set<AutoCompleteEntry> templates = lm.getTemplates();
		for(AutoCompleteEntry entry : templates) {
			choiceItems.add(entry);
		}
		showMenue();
	}
	/**
	 * Beendet die Auswahl.
	 */
	private void stop() {
		active = false;
		choiceItems.clear();
		choiceWindow.setVisible(false);
		choiceList.removeAll();
	}
	/**
	 * Berechnet und setzt die Position die das Auswahlfenster abhängig
	 * von der Cursorposition haben muss.
	 */
	private void setPosition() {
		Rectangle box;
		try {
			box = component.modelToView(startPosition-1);
		} catch (BadLocationException e) {
			box = new Rectangle(0,0);
		}		

		int windowX = component.getLocationOnScreen().x + box.x;
		int windowY = component.getLocationOnScreen().y + + box.y + box.height;
		choiceWindow.setLocation(
				windowX, 
				windowY
		);		
	}
	/**
	 * Wendet die aktuell ausgewählte Option im Text an.
	 */
	private void complete() {
		AutoCompleteInformation choice = choiceList.getSelectedValue();
		Document text = component.getDocument();
		stop(); //stop before changing in order to avoid feedback
		if (choice != null) {
			try {
				((AbstractDocument)text).replace(startPosition, lastPosition - startPosition, choice.getTemplate(), null);
				int openBracket = startPosition + choice.getTemplate().indexOf("(") + 1;
				if (openBracket != startPosition) {
					component.setCaretPosition(openBracket);
				}
			} catch (BadLocationException e) {
				//we can't do anything useful, but nothing
			}
		}
	}
	/**
	 * Aktuallisiert die Auswahlmöglichkeiten anhand der Eingage. 
	 */
	private void updateChoices() {
		Document text = component.getDocument();
		try {
			AutoCompleteInformation selected = choiceList.getSelectedValue();
			String prefix = text.getText(startPosition, lastPosition - startPosition).toLowerCase();
			AutoCompleteEntry[] choices = choiceItems.stream().filter(choice -> choice.getScantext().toLowerCase().startsWith(prefix)).toArray(size -> new AutoCompleteEntry[size]);
			if (choices.length == 0) {
				stop();
			} else {
				choiceList.setListData(choices);
				if (selected != null) {
					choiceList.setSelectedValue(selected, true);
					if (choiceList.getSelectedValue() == null) {
						choiceList.setSelectedIndex(0);
					}
				} else {
					choiceList.setSelectedIndex(0);
				}
			}
		} catch (BadLocationException e) {
			stop();
			return;
		}
	}
	/**
	 * Setzt die gewählte Option.
	 * @param index Index der neuen Auswahl
	 */
	private void setSelectedItem(int index) {
		if (choiceList.getModel().getSize() == 0) return;
		int n = index % choiceList.getModel().getSize();
		if (n < 0) {
			n += choiceList.getModel().getSize();
		}
		choiceList.setSelectedIndex(n);
		choiceList.ensureIndexIsVisible(n);
		
	}
	private class TooltipList extends JList<AutoCompleteInformation> {
		private static final long serialVersionUID = -7313420722740426372L;

		public TooltipList() {
			ToolTipManager.sharedInstance().registerComponent(this);
		}

		@Override
		public String getToolTipText(MouseEvent me) {
			 int index = locationToIndex(me.getPoint());
			 if (index >= 0) {
				 return "<html><p>"+getModel().getElementAt(index).getTooltip()+"</p></html>";
			 } else {
				 return null;
			 }
		}	
	}
	/**
	 * Anzeige Komponente für eine einzelne Auswahlmöglichkeit.
	 * @author Peter (Lathanda) Schneider
	 *
	 */
	private static class ChoiceCellRenderer extends JLabel implements ListCellRenderer<AutoCompleteInformation> {
		private static final long serialVersionUID = -6215568900839124763L;

	     @Override
	     public Component getListCellRendererComponent(
	    		 JList<? extends AutoCompleteInformation> list, 
	    		 AutoCompleteInformation value, 
	    		 int index,
	    		 boolean isSelected, 
	    		 boolean cellHasFocus)
	     {
	         setText(value.getLabel());
        	 setIcon(AutoCompleteInformation.ICON[value.getType()]);
	         if (isSelected) {
	             setBackground(Color.BLUE);
	             setForeground(Color.WHITE);
	         } else {
	             setBackground(Color.WHITE);
	             setForeground(Color.BLACK);
	         }
	         setEnabled(list.isEnabled());
	         setFont(list.getFont());
	         setOpaque(true);
	         return this;
	     }

	 }	
	@Override
	public void caretUpdate(CaretEvent ce) {
		if (!active) return;
		lastPosition = ce.getDot();
		if (lastPosition < startPosition) {
			stop();
		} else {
			updateChoices();
		}
	}

	@Override
	public void keyPressed(KeyEvent ke) {
		if (ke.isControlDown() && ke.getKeyCode() == KeyEvent.VK_SPACE) {
			ke.consume();
			startTemplate();
			consumeNextKey = true;
			return;
		}
		if (!active) return;

		switch(ke.getKeyCode()) {
		case KeyEvent.VK_UP:
			setSelectedItem(choiceList.getSelectedIndex() - 1);			
			ke.consume();
			break;
		case KeyEvent.VK_DOWN:
			setSelectedItem(choiceList.getSelectedIndex() + 1);
			ke.consume();
			break;
		case KeyEvent.VK_ENTER:
		case KeyEvent.VK_TAB:
			complete();
			ke.consume();
		case KeyEvent.VK_ESCAPE:
			stop();
			ke.consume();
		}
		
	}

	@Override
	public void keyReleased(KeyEvent ke) {}

	@Override
	public void keyTyped(KeyEvent ke) {
		if (!active) return;
		if(consumeNextKey) {
			consumeNextKey = false;
			ke.consume();
			return;
		}
		//any none alphabetic character will stop code completion
		if (!(Character.isAlphabetic(ke.getKeyChar()) || ke.getKeyChar() == '\b')) {
			stop();
		}		
	}

	@Override
	public void focusGained(FocusEvent fe) {}

	@Override
	public void focusLost(FocusEvent fe) {
		if (!active) return;
		stop();		
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
		if (!active) return;
		stop();
	}

	@Override
	public void componentMoved(ComponentEvent ce) {
		if (!active) return;
		setPosition();				
	}

	@Override
	public void componentResized(ComponentEvent ce) {
		if (!active) return;
		setPosition();		
	}

	@Override
	public void componentShown(ComponentEvent ce) {}

	@Override
	public void insertString(int pos, String text, AbstractProgram program) {
		try {
			if (text.equals(".")) {
				AbstractType base = program.seekType(pos);
				start(base, pos + 1);
			} else if (text.equals(":")) {
				start(Type.getClassType(), pos + 1);
			}
		} catch (BadLocationException e) {
			//ignore it
		}
	}
}
