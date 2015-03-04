package rabat.start;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.robotics.RegulatedMotor;

/**
 * follow the black path using one sensor
 * @author Ohad Cohen & Ron Cohen
 *
 */
public class Lightner {

	private static final RegulatedMotor motorL = Motor.A;
	private static final RegulatedMotor motorR = Motor.C;
	
	private static double wheelDiameter = 56;
	private static double wheelDist = 170;
	private static int rotateAngle = (int)(360*wheelDist/(wheelDiameter*2));
	
	private static LightSensor sens = new LightSensor(SensorPort.S3);
	
	private static int fixFactor = 50;
	
	private static int avgLight = (sens.getHigh() - sens.getLow())/2;
	private static int errLight = avgLight / 50;
	private static int minLight = avgLight + errLight;
	private static int maxLight = avgLight - errLight;
	
	private static void fix(RegulatedMotor motor, String print, int times) {
		int speed = motor.getSpeed();
		int maxSpeed = (int) motor.getMaxSpeed();
		times=3;
		for(int i=0;i<times; i++) {
			motor.stop(true);
			motor.setSpeed(maxSpeed/100);
			motor.forward();
			LCD.drawString(print + times + " " + i + "                   ", 1, 2);
		}
		motor.stop(true);
		motor.setSpeed(speed);
		motor.forward();			
		LCD.drawString("                    ", 1, 2);
	}

	public static void main(String[] args) {
		final int speed = (int) (motorL.getMaxSpeed()/2);
		int light;
		motorL.resetTachoCount();
		motorR.resetTachoCount();
		
		motorL.setSpeed(speed);
		motorR.setSpeed(speed);

		LCD.drawString("speed "+speed ,1,1);

//		Button.waitForAnyPress();
		
		while(true) {
			motorL.forward();
			motorR.forward();
			// If white
			if ((light = sens.getNormalizedLightValue()) > maxLight) {
				fix(motorL, "fix left", (light - avgLight)/fixFactor);
			}
			// If black
//			

			
		}
				
	}

}
