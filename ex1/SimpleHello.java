package rabat;

import lejos.nxt.*;
import lejos.robotics.RegulatedMotor;

public class SimpleHello {

	private static final RegulatedMotor motorR = Motor.B;
	private static final RegulatedMotor motorL = Motor.C;
	
	private static final int rotatDist = 25;
	private static final int errorDist = 3;	
		
	private static final UltrasonicSensor sensR = new UltrasonicSensor(SensorPort.S1);
	private static final UltrasonicSensor sensF = new UltrasonicSensor(SensorPort.S4);

	private static double wheelDiameter = 56;
	private static double wheelDist = 105;
	private static int rotateAngle = (int)(360*wheelDist/(wheelDiameter*2));
	
	
	private static void fix(RegulatedMotor motor, String print, int times) {
		int speed = motor.getSpeed();
		for(int i=0;i<times; i++) {
			motor.stop(true);
			motor.setSpeed(speed/10);
			LCD.drawString(print, 1, 1);
			motor.forward();
		}
		motor.stop(true);
		motor.setSpeed(speed);
		motor.forward();			
	}
	
	public static void main(String[] args) {
		final int avgDistance = 15;// sensR.getDistance();
		final int maxDistance = avgDistance + errorDist;
		final int minDistance = avgDistance - errorDist;
		
		final int speed = (int) motorL.getMaxSpeed()/2;
		final int rotateSpeed = (speed)/3;
		
		LCD.drawString("startint at speed "+speed ,1,2);
		LCD.drawString("and rotating at " +rotateSpeed, 1, 3);
		LCD.drawString("on distance " + avgDistance, 1, 3);
		
		boolean stop = false;
		int rotation = 0;
		
		motorL.setSpeed(speed);
		motorR.setSpeed(speed);
		
		while(!stop) {
			motorL.forward();
			motorR.forward();
			if (sensF.getDistance()<rotatDist) {
				motorL.stop(true);
				motorR.stop(true);
				LCD.drawString("rotating", 1, 1);
				if(sensF.getDistance()<rotatDist) {
					motorR.rotate(rotateAngle);
					if(++rotation == 4)
						stop = true;
				}
				motorL.forward();
				motorR.forward();
			}
			
			if( sensR.getDistance() > maxDistance ){
				fix(motorR, "fix right", sensR.getDistance() - avgDistance);
			}
			
			if( sensR.getDistance() < minDistance){
				fix(motorL, "fix left",  avgDistance - sensR.getDistance());
			}

		}
		LCD.drawString("stoping...", 1, 1);
		motorL.stop(true);
		motorR.stop(true);

	}

}
