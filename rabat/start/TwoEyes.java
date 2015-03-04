package rabat.start;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.RegulatedMotor;

/**
 * follow a black marked path, using two sensors
 * @author Ohad Cohen & Ron Cohen
 *
 */
public class TwoEyes {

	private static final RegulatedMotor motorL = Motor.A;
	private static final RegulatedMotor motorR = Motor.C;
	
	private static double wheelDiameter = 56;
	private static double wheelDist = 170;
	private static int rotateAngle = (int)(360*wheelDist/(wheelDiameter*2));
	
	private static LightSensor sensR = new LightSensor(SensorPort.S3);
	private static LightSensor sensL = new LightSensor(SensorPort.S2);
	
	private static int fixFactor = 1;
	
	private static int avgLight = (sensR.getHigh() - sensR.getLow())/2;
//	int i =sens.
	private static int errLight = avgLight / 10;
	private static int minLight = sensR.getLow() + errLight;
	private static int maxLight = sensR.getHigh() - errLight;
	
	private static void fix(RegulatedMotor motor, String print, int times) {
		int speed = motor.getSpeed();
		times=3;
		for(int i=0;i<times; i++) {
			motor.stop(true);
			motor.setSpeed(0);
			motor.forward();
			LCD.drawString(print + " " + times + " " + i + "                   ", 1, 2);
		}
		motor.stop(true);
		motor.setSpeed(speed);
		motor.forward();			
		LCD.drawString("                    ", 1, 2);
	}

	public static void main(String[] args) {
		final int speed = (int) (motorR.getMaxSpeed());
		int lightR, lightL;
		motorL.resetTachoCount();
		motorR.resetTachoCount();
		
		motorL.setSpeed(speed);
		motorR.setSpeed(speed);

		motorL.forward();
		motorR.forward();
		
		LCD.drawString("speed "+speed ,1,1);
		LCD.drawString("Two Eyes! " + avgLight ,1,3);

//		Button.waitForAnyPress();
		
		while(true) {
			if ((lightR=sensR.getNormalizedLightValue()) > minLight + errLight) {
				fix(motorR, "fix right",(lightR - minLight) /fixFactor);
			}
			if((lightL = sensL.getNormalizedLightValue()) < maxLight - errLight) {
				fix(motorL, "fix left", (maxLight - lightL)/fixFactor);
			}

			
		}
				
	}

}
