package de.lathanda.eos.common.gui;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.ResourceBundle;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import de.lathanda.eos.common.interpreter.AbstractProgram;
import de.lathanda.eos.util.GuiToolkit;


/**
 * Druckvorschaufenster
 *
 * @author Peter (Lathanda) Schneider
 */
public class PrintFrame extends JFrame {

	private static final long serialVersionUID = -2002251247920181865L;
	/**
     * Creates new form PrintFrame
     */
    public PrintFrame(String title) {
        initComponents(title);
    }
    public void init(AbstractProgram abstractProgram) {
        printview.init(abstractProgram, txtTitle.getText());
    }

    private void initComponents(String title) {

        controlToolbar = new JToolBar();
        left = new JPanel();
        right = new JPanel();
        center = new JPanel();
        controlToolbar.setLayout(new BorderLayout());
        controlToolbar.add(left, BorderLayout.WEST);
        controlToolbar.add(right, BorderLayout.EAST);
        controlToolbar.add(center, BorderLayout.CENTER);
        chkLinenumbers = GuiToolkit.createCheckBox(Messages.getString("Print.Linenumbers"));
        txtTitle = GuiToolkit.createTextField();
        
        printScroll = new JScrollPane();
        printview = new PrintPanel();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        ResourceBundle bundle = ResourceBundle.getBundle("text.gui");
        setTitle(bundle.getString("Print.Title"));
        
        controlToolbar.setFloatable(false);
        controlToolbar.setRollover(true);

        btnLeft = GuiToolkit.createButton("icons/navigate_left.png", null, evt -> btnLeftActionPerformed(evt));

        left.add(btnLeft);

        btnRight = GuiToolkit.createButton("icons/navigate_right.png", null, evt -> btnRightActionPerformed(evt));
        
        left.add(btnRight);

        chkLinenumbers.setFocusable(false);
        chkLinenumbers.addChangeListener(evt -> chkLinenumbersChanged(evt));                
        
        left.add(chkLinenumbers);                
              
        txtTitle.addActionListener(ae -> printview.setHeader(txtTitle.getText()));
        txtTitle.setText(title);
        left.add(txtTitle);
        
        btnPrint = GuiToolkit.createButton("icons/printer.png", null, evt -> btnPrintActionPerformed(evt));

        right.add(btnPrint);

        GroupLayout printviewLayout = new GroupLayout(printview);
        printview.setLayout(printviewLayout);

        printScroll.setViewportView(printview);
        printScroll.setPreferredSize(GuiToolkit.scaledDimension(400, 500));
        
        getContentPane().add(controlToolbar,  BorderLayout.NORTH);
        getContentPane().add(printScroll, BorderLayout.CENTER);
        pack();
    }

    private void btnPrintActionPerformed(ActionEvent evt) {
        try {
        	printview.setHeader(txtTitle.getText());
            PrinterJob job = PrinterJob.getPrinterJob();
            job.setPageable(printview);
            if (!job.printDialog()) {
                return;
            }
            job.print();
        } catch (HeadlessException | PrinterException t) {
            JOptionPane.showMessageDialog(this, Messages.getString("Print.Error.Title"),
                t.getLocalizedMessage(),
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void btnLeftActionPerformed(ActionEvent evt) {
        printview.previousPage();
    }

    private void btnRightActionPerformed(ActionEvent evt) {
        printview.nextPage();
    }
	public void chkLinenumbersChanged(ChangeEvent e) {
		printview.setLinenumberVisible(chkLinenumbers.isSelected());
		
	}

    private JButton btnLeft;
    private JButton btnPrint;
    private JButton btnRight;
    private JCheckBox chkLinenumbers;
    private JTextField txtTitle;
    private JToolBar controlToolbar;
    private JScrollPane printScroll;
    private PrintPanel printview;
    private JPanel left;
    private JPanel right;
    private JPanel center;
}
