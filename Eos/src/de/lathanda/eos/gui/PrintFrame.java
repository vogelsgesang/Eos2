package de.lathanda.eos.gui;

import static de.lathanda.eos.gui.GuiConstants.GUI;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import de.lathanda.eos.interpreter.Program;


/**
 * Druckvorschaufenster
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.8
 */
public class PrintFrame extends javax.swing.JFrame {

	private static final long serialVersionUID = -2002251247920181865L;
	/**
     * Creates new form PrintFrame
     */
    public PrintFrame() {
        initComponents();
    }
    public void init(Program program) {
        printview.init(program);
    }

    private void initComponents() {

        controlToolbar = new javax.swing.JToolBar();
        btnLeft = new javax.swing.JButton();
        btnRight = new javax.swing.JButton();
        space1 = new javax.swing.Box.Filler(new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 0), new java.awt.Dimension(20, 32767));
        btnPrint = new javax.swing.JButton();
        printScroll = new javax.swing.JScrollPane();
        printview = new de.lathanda.eos.gui.PrintPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        ResourceBundle bundle = ResourceBundle.getBundle("text.gui");
        setTitle(bundle.getString("Print.Title"));
        
        controlToolbar.setFloatable(false);
        controlToolbar.setRollover(true);

        btnLeft.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrow-left-2.png")));
        btnLeft.setFocusable(false);

        btnLeft.addActionListener(evt -> btnLeftActionPerformed(evt));

        controlToolbar.add(btnLeft);

        btnRight.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/arrow-right-2.png")));
        btnRight.setFocusable(false);
        btnRight.addActionListener(evt -> btnRightActionPerformed(evt));

        controlToolbar.add(btnRight);
        controlToolbar.add(space1);

        btnPrint.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/document-print-3.png")));
        btnPrint.addActionListener(evt -> btnPrintActionPerformed(evt));

        controlToolbar.add(btnPrint);

        javax.swing.GroupLayout printviewLayout = new javax.swing.GroupLayout(printview);
        printview.setLayout(printviewLayout);

        printScroll.setViewportView(printview);
        printScroll.setPreferredSize(new Dimension(500, 600));
        
        getContentPane().add(controlToolbar,  BorderLayout.NORTH);
        getContentPane().add(printScroll, BorderLayout.CENTER);
        pack();
    }

    private void btnPrintActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPageable(printview);
            if (!job.printDialog()) {
                return;
            }
            job.print();
        } catch (HeadlessException | PrinterException t) {
            JOptionPane.showMessageDialog(this, GUI.getString("Print.Error.Title"),
                t.getLocalizedMessage(),
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void btnLeftActionPerformed(java.awt.event.ActionEvent evt) {
        printview.previousPage();
    }

    private void btnRightActionPerformed(java.awt.event.ActionEvent evt) {
        printview.nextPage();
    }

    private javax.swing.JButton btnLeft;
    private javax.swing.JButton btnPrint;
    private javax.swing.JButton btnRight;
    private javax.swing.JToolBar controlToolbar;
    private javax.swing.JScrollPane printScroll;
    private de.lathanda.eos.gui.PrintPanel printview;
    private javax.swing.Box.Filler space1;
}
