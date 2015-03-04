package rabat;

import javax.microedition.sensor.HiTechnicGyroSensorInfo;

import rabat.alpha.MuxPort;
import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.CompassHTSensor;
import lejos.nxt.addon.RCXLightSensor;
import lejos.nxt.addon.RCXSensorMultiplexer;
import lejos.nxt.addon.SensorMux;
import lejos.nxt.rcxcomm.RCXPort;

/**
 * Exercise specific and cross exercises constants
 * @author Ohad Cohen <ohadcn@cs.huji.ac.il>
 * @author Ron Cohen
 *
 */
public class Constants {
	public static final double dist = 9;
	
	public static final double blockSize = 31;
	public static final int sizeX = 6;
	public static final int sizeY = 4;
	
	public static final int cutoffLight = 47;
	public static final int cutoffDist = 250;
	
	public static final int FORWARD = 1;
	public static final int LEFT = 2;
	public static final int BACK = 4;
	public static final int RIGHT = 8;
	
	public static final int[] DIRECTIONS = {FORWARD, LEFT, BACK, RIGHT};
	public static final int[] reverseTable={0,4,8,12,1,5,9,13,2,6,10,14,3,7,11,15};
	
	public static final int AVALIABLE = 16;
	public static final int VISITED = 32;
	public static final int TARGET = 64;
	public static final int START = 128;
	
	public static RCXSensorMultiplexer mux = new RCXSensorMultiplexer(SensorPort.S3);
//	public static RCXPort rcx = new RCXPort(SensorPort.S1);
//	public static UltrasonicSensor sensR = new UltrasonicSensor(new RCXPort())
//	public static CompassHTSensor compass=new CompassHTSensor(mux.getPort(), 0x10);

	public static UltrasonicSensor sensR=new UltrasonicSensor(SensorPort.S1);
	public static UltrasonicSensor sensF=new UltrasonicSensor(SensorPort.S2);
	public static UltrasonicSensor sensL=new UltrasonicSensor(SensorPort.S4);
//	public static ColorSensor light=new ColorSensor(SensorPort.S3);
	public static LightSensor light=new LightSensor(SensorPort.S3);
//	public static RCXLightSensor light=new RCXLightSensor(SensorPort.S3);
	public static CompassHTSensor compass=new CompassHTSensor(new MuxPort(mux,1));
	
	
	public static final double wheelDiamet = 5.6;
	public static final double wheelDist = 11.6;
	
	public static final int boardDrawWidth = (int)(LCD.SCREEN_WIDTH*2/3.0); 
	public static final int boardDrawHeight = (int)(LCD.SCREEN_HEIGHT*2/3.0); 
}
