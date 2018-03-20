package de.lathanda.eos.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.FontUIResource;

import de.lathanda.eos.base.ResourceLoader;

/**
 * Sammlung von häufig verwendeten Funktionen für die grafische Oberfläche.
 *
 * @author Peter (Lathanda) Schneider
 */
public class GuiToolkit {
	/**
	 * Die vermutliche Bildschirmauflösung.
	 * 
	 * Die Betriebssysteme kennen die exakte Bildschirmauflösung nur ungefähr und ab
	 * und zu wird absichtlich gelogen. (zB Dynamic Super Resolution).
	 */
	private static int dpi = Toolkit.getDefaultToolkit().getScreenResolution();
	private static double unit = 1;
	private static final Font MENU_ITEM_FONT = createFont(Font.SANS_SERIF, Font.PLAIN, 10);
	private static final Font MENU_FONT   = createFont(Font.SANS_SERIF, Font.PLAIN, 10);
	private static final Font LABEL_FONT  = createFont(Font.SANS_SERIF, Font.PLAIN, 10);
	private static final Font SLIDER_FONT = createFont(Font.SANS_SERIF, Font.PLAIN, 8);
	private static final Font TITLE_BORDER_FONT = createFont(Font.SANS_SERIF, Font.PLAIN, 8);
	private static final int CMD_MASK = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

	/**
	 * Millimeter in Pixel umrechnen.
	 * 
	 * @param mm
	 * @return
	 */
	public static int mm2pixel(double mm) {
		return (int) (dpi / 25.4f * mm);
	}

	/**
	 * Pixel in Millimeter umrechnen.
	 * 
	 * @param pixel
	 * @return
	 */
	public static double pixel2mm(int pixel) {
		return pixel * 25.4f / dpi;
	}

	static {
		UIManager.getLookAndFeelDefaults().put("ToolTip.font",
				new FontUIResource(Font.SANS_SERIF, Font.PLAIN, dpi * 10 / 72));
	}
	public static int getScreenResolution() {
		return dpi;
	}
	public static void setScreenResolution(int dpi) {
		if (dpi == 0) {
			dpi = Toolkit.getDefaultToolkit().getScreenResolution();
		} else {
			GuiToolkit.dpi = dpi;
		}
	}
	public static double getUnit() {
		return unit;
	}
	public static void setUnit(double unit) {
		GuiToolkit.unit = unit;
	}
	public static Font createFont(String name, int style, int size) {
		return new Font(name, style, getFontSize(size));
	}

	public static int getFontSize(int size) {
		return dpi * size / 72;
	}

	public static int scaledSize(int size) {
		return dpi * size / 72;
	}
	public static Dimension scaledDimension(int width, int height) {
		return new Dimension(dpi * width / 72, dpi * height / 72);
	}
	
	public static JButton createButton(String image, String tooltip, ActionListener action) {
		JButton btn = new JButton();
		ImageIcon icon = null;
		Image i = ResourceLoader.loadImage(image);
		icon = new ImageIcon(i.getScaledInstance(dpi * 40 / 96, dpi * 40 / 96, Image.SCALE_SMOOTH));

		btn.setIcon(icon);
		btn.setToolTipText(tooltip);
		btn.setFocusable(false);
		btn.addActionListener(action);
		return btn;
	}

	public static JMenu createMenue(String text) {
		JMenu menu = new JMenu(text);
		menu.setFont(MENU_FONT);
		return menu;
	}

	public static JMenuItem createMenuItem(String text, String tooltip, ActionListener action) {
		JMenuItem mit = new JMenuItem();
		mit.setText(text);
		if (tooltip != null) {
			mit.setToolTipText(tooltip);
		}
		mit.addActionListener(action);
		mit.setFont(MENU_ITEM_FONT);
		return mit;
	}

	public static JMenuItem createMenuItem(String text, String tooltip, ActionListener action, int mem) {
		JMenuItem mit = createMenuItem(text, tooltip, action);
		mit.setAccelerator(KeyStroke.getKeyStroke(mem, CMD_MASK));
		return mit;
	}

	public static JSlider createSlider(String text, ChangeListener change) {
		JSlider slider = new JSlider();
		slider.setFocusable(false);
		slider.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(), text, TitledBorder.CENTER,
				TitledBorder.TOP, SLIDER_FONT));
		slider.addChangeListener(change);
		return slider;
	}
	public static TitledBorder createTitledBorder(String title) {
		return BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK), title, TitledBorder.CENTER,
				TitledBorder.TOP, TITLE_BORDER_FONT);
	}

	public static JLabel createLabel(String text) {
		JLabel label = new JLabel(text);
		label.setFont(LABEL_FONT);
		return label;
	}

	public static JCheckBox createCheckBox(String label) {
		JCheckBox checkbox = new JCheckBox(label, false);
		checkbox.setFont(LABEL_FONT);
		return checkbox;
	}
	
	public static JTextField createTextField() {
		JTextField textfield = new JTextField();
		textfield.setFont(LABEL_FONT);
		return textfield;
	}

	public static ImageIcon createSmallIcon(String image) {
		ImageIcon icon = null;
		Image i = ResourceLoader.loadImage(image);
		icon = new ImageIcon(i.getScaledInstance(dpi * 16 / 96, dpi * 16 / 96, Image.SCALE_SMOOTH));
		return icon;
	}
}
