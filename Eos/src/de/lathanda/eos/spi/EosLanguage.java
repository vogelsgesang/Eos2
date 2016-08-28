package de.lathanda.eos.spi;

import static de.lathanda.eos.common.gui.GuiConstants.GUI;

import java.awt.Color;
import java.io.IOException;
import java.util.ResourceBundle;

import de.lathanda.eos.base.Alignment;
import de.lathanda.eos.base.FillStyle;
import de.lathanda.eos.base.LineStyle;
import de.lathanda.eos.geo.Circle;
import de.lathanda.eos.geo.Ellipse;
import de.lathanda.eos.geo.Figure;
import de.lathanda.eos.geo.FilledFigure;
import de.lathanda.eos.geo.Graphic;
import de.lathanda.eos.geo.Group;
import de.lathanda.eos.geo.Line;
import de.lathanda.eos.geo.LineFigure;
import de.lathanda.eos.geo.Plotter;
import de.lathanda.eos.geo.Rectangle;
import de.lathanda.eos.geo.Square;
import de.lathanda.eos.geo.TextField;
import de.lathanda.eos.geo.Triangle;
import de.lathanda.eos.geo.Window;
import de.lathanda.eos.interpreter.SystemFunctions;
import de.lathanda.eos.interpreter.Type.ObjectSource;
/**
 * \brief Spracherweiterung Basissprache
 * 
 * Diese Erweiterung stellt alle Basis EOS Klassen zur Verfügung.
 * Sie dient als Vorlage für andere Erweiterungen.
 * 
 *
 * @author Peter (Lathanda) Schneider
 * @since 0.9.7
 */
public class EosLanguage /*extends Language*/ implements LanguageProvider {

	public void registerLanguage(LanguageManager lm) throws IOException {
		//init diagrams
		lm.registerLabels(ResourceBundle.getBundle("text.label"));
		lm.registerNames(ResourceBundle.getBundle("text.map_names"));
		//init help
		try {
			lm.setHelp(GUI.getString("Help.Topics"));
		} catch (IOException e) {
			//hmm no help, we probably will survive that			
		}
		//load config
		ResourceBundle lang = ResourceBundle.getBundle("text.lang");
		
		//create technical klasse   		
   		lm.registerType("class", null, "", null, null);	
   		lm.registerType("#", null, lang.getString("#"), null, null);
   		lm.registerType("?", null, lang.getString("?"), null, null);
   		lm.registerInherits("class", getInherits("class"));
   		lm.registerInherits("#", getInherits("#"));
   		lm.registerInherits("?", getInherits("?"));
   		
   		//create other classes
   		lm.registerLanguageByConfig(lang, this);
	}

	public ObjectSource getObjectSource(String id) {
		switch (id) {
		case "alignment":
			return () -> {
				return Alignment.CENTER;
			};
		case "boolean":
			return () -> {
				return false;
			};
		case "color":
			return () -> {
				return Color.BLACK;
			};
		case "real":
			return () -> {
				return 0d;
			};

		case "fillstyle":
			return () -> {
				return FillStyle.FILLED;
			};
		case "integer":
			return () -> {
				return 0;
			};
		case "grafic":
			return () -> {
				return new Graphic();
			};
		case "linestyle":
			return () -> {
				return LineStyle.SOLID;
			};
		case "linefigure":
			return null;
		case "window":
			return () -> {
				return new Window();
			};
		case "triangle":
			return () -> {
				return new Triangle();
			};
		case "textfield":
			return () -> {
				return new TextField();
			};
		case "square":
			return () -> {
				return new Square();
			};
		case "rectangle":
			return () -> {
				return new Rectangle();
			};
		case "string":
			return () -> {
				return "";
			};
		case "plotter":
			return () -> {
				return new Plotter();
			};
		case "line":
			return () -> {
				return new Line();
			};
		case "group":
			return () -> {
				return new Group();
			};
		case "filledfigure":
			return null;
		case "figure":
			return null;
		case "ellipse":
			return () -> {
				return new Ellipse();
			};
		case "circle":
			return () -> {
				return new Circle();
			};
		default:
			return null;
		}
	}

	public String[] getInherits(String id) {
		switch (id) {
		case "alignment":
			return new String[]{"alignment","string"};
		case "boolean":
			return new String[]{"boolean","string"};
		case "color":
			return new String[]{"color","string"};
		case "real":
			return new String[]{"real","integer", "string"};
		case "fillstyle":
			return new String[]{"fillstyle","string"};
		case "integer":
			return new String[]{"integer","real","string"};
		case "linestyle":
			return new String[]{"linestyle", "string"};
		case "linefigure":
			return new String[]{"linefigure","figure", "string"};
		case "window":
			return new String[]{"window","string"};
		case "triangle":
			return new String[]{"triangle","filledfigure","linefigure","figure","string"};
		case "textfield":
			return new String[]{"textfield","rectangle","filledfigure","linefigure","figure","string"};
		case "square":
			return new String[]{"square","filledfigure","linefigure","figure","string"};
		case "rectangle":
			return new String[]{"rectangle","filledfigure","linefigure","figure","string"};
		case "grafic":
			return new String[]{"grafic","rectangle","filledfigure","linefigure","figure","string"};
		case "string":
			return new String[]{"string"};
		case "plotter":
			return new String[]{"plotter","figure","string"};
		case "line":
			return new String[]{"line","linefigure","figure","string"};
		case "group":
			return new String[]{"group","figure","string"};
		case "filledfigure":
			return new String[]{"filledfigure","linefigure","figure","string"};
		case "figure":
			return new String[]{"figure","string"};
		case "ellipse":
			return new String[]{"ellipse","filledfigure","linefigure","figure","string"};
		case "circle":
			return new String[]{"circle","filledfigure","linefigure","figure","string"};
		default:
			return new String[]{id};
		}
	}
	public Class<?> getClassById(String id) {
		switch (id) {
		case "alignment":
			return Alignment.class;
        case "boolean":
            return boolean.class;
        case "color":
            return Color.class;
        case "real":
            return double.class;
        case "fillstyle":
            return FillStyle.class;
        case "integer":
            return int.class;
        case "grafic":
        	return Graphic.class;
        case "linestyle":
            return LineStyle.class;
        case "linefigure":
            return LineFigure.class;
        case "window":
            return Window.class;
        case "triangle":
            return Triangle.class;
        case "textfield":
            return TextField.class;
        case "square":
            return Square.class;
        case "rectangle":
            return Rectangle.class;
        case "string":
            return String.class;
        case "plotter":
            return Plotter.class;
        case "line":
            return Line.class;
        case "group":
            return Group.class;
        case "filledfigure":
            return FilledFigure.class;
        case "figure":
            return Figure.class;
        case "ellipse":
            return Ellipse.class;
        case "circle":
            return Circle.class;
        default:
            return null;
		}
		
	}

	@Override
	public Class<?> getFunctionTarget() {
		return SystemFunctions.class;
	}
}
