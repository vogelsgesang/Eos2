package de.lathanda.legoev3.bytecode;

public interface ColorSensorMode {
	byte REFLECTION = SensorMode.MODE0; 
	byte AMBIENT    = SensorMode.MODE1;
	byte COLOR      = SensorMode.MODE2;
	byte RAW        = SensorMode.MODE3;
	byte NXT_GREEN  = SensorMode.MODE3;
	byte NXT_BLUE   = SensorMode.MODE4;

}
