package de.lathanda.eos.gui.diagram;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;

import de.lathanda.eos.common.gui.BackgroundCompiler;
import de.lathanda.eos.common.gui.ClipBoard;
import de.lathanda.eos.util.GuiToolkit;

/**
 * Fenster für Diagramme.
 *
 * @author Peter (Lathanda) Schneider
 */
public class DiagramFrame extends JFrame implements WindowListener {
	private static final long serialVersionUID = -2735991664797231612L;
	BackgroundCompiler bc;
    Diagram diagram;

    /**
     * Erzeugt ein Diagrammfenster
     * @param diagram Das Diagramm das dargestellt werden soll.
     * @param bc Der Hintergrundkompiler.
     */
    public DiagramFrame(Diagram diagram, BackgroundCompiler bc) {
        super(diagram.getTitle());
        this.diagram = diagram;
        this.bc = bc;
        diagram.init(bc);   
        initComponents();          
    }
    /**
     * Initialisiert alle Komponenten.
     */
    private void initComponents() {

        diagramToolbar = new javax.swing.JToolBar();
        btnClipboard = GuiToolkit.createButton("icons/clipboard.png", null, evt -> btnClipboardActionPerformed(evt));
        scroll = new javax.swing.JScrollPane();
        scroll.setPreferredSize(new Dimension(400, 400));
        scroll.setViewportView(diagram);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(this);

        diagramToolbar.setFloatable(false);
        diagramToolbar.setRollover(true);
        diagramToolbar.add(btnClipboard);

        getContentPane().add(diagramToolbar, BorderLayout.NORTH);
        getContentPane().add(scroll, BorderLayout.CENTER);

        pack();
    }
    /**
     * Kopiert das Diagramm in die Zwischenablage.
     * @param evt
     */
    private void btnClipboardActionPerformed(java.awt.event.ActionEvent evt) {
        ClipBoard.pushImage(diagram.export(300f));
    }
    
    private javax.swing.JButton btnClipboard;
    private javax.swing.JToolBar diagramToolbar;
    private javax.swing.JScrollPane scroll;
	@Override
	public void windowActivated(WindowEvent we) {}
	@Override
	public void windowClosed(WindowEvent we) {}
	@Override
	public void windowClosing(WindowEvent we) {
        diagram.deinit(bc);
        setVisible(false);
        dispose();
	}
	@Override
	public void windowDeactivated(WindowEvent we) {}
	@Override
	public void windowDeiconified(WindowEvent we) {}
	@Override
	public void windowIconified(WindowEvent we) {}
	@Override
	public void windowOpened(WindowEvent we) {}

}
