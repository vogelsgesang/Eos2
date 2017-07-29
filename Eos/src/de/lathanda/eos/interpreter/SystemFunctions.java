package de.lathanda.eos.interpreter;

import java.util.Calendar;
import java.util.Random;
/**
 * Globales Eos Funktionen.
 * 
 * @author Peter (Lathanda) Schneider 
 */
public abstract class SystemFunctions {
    private static final Random random = new Random();
    public static int round(double z) {
        return (int)Math.round(z);
    }
    public static int floor(double z) {
        return (int)Math.floor(z);
    }
    public static int random(int von, int bis) {
        return random.nextInt(bis - von + 1) + von;
    }
    public static double abs(double z) {
        return Math.abs(z);
    }
    public static double sqrt(double z) {
        return Math.sqrt(z);
    }
    public static double sin(double alpha) {
        return Math.sin(alpha/180*Math.PI);
    }
    public static double cos(double alpha) {
        return Math.cos(alpha/180*Math.PI);        
    }
    public static double tan(double alpha) {
        return Math.tan(alpha/180*Math.PI);
    }
    public static double arctan(double m) {
        return Math.atan(m)*180/Math.PI;
    }
    public static double arcsin(double m) {
        return Math.asin(m)*180/Math.PI;
    }
    public static double arccos(double m) {
        return Math.acos(m)*180/Math.PI;
    }
    public static double phi(double x, double y) {
        double phi = Math.atan(y/x);
        if (x < 0) {
            return phi*180/Math.PI + 180;
        } else {
            return phi*180/Math.PI;
        }
    }
    public static double r(double x, double y) {
        return Math.sqrt(x*x+y*y);
    }
    public static int hour() {
    	return Calendar.getInstance().get(Calendar.HOUR);
    }
    public static int minute() {
    	return Calendar.getInstance().get(Calendar.MINUTE);
    }
    public static int second() {
    	return Calendar.getInstance().get(Calendar.SECOND);
    }
    public static int year() {
    	return Calendar.getInstance().get(Calendar.YEAR);
    }
    public static int month() {
    	return Calendar.getInstance().get(Calendar.MONTH);
    }
    public static int day() {
    	return Calendar.getInstance().get(Calendar.DATE);
    }
    	
    
    public SystemFunctions() {}
}
