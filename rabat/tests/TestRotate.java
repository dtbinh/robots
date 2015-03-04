package rabat.tests;

import rabat.Constants;
import rabat.alpha.DifferentialPilot;
import lejos.nxt.Button;
import lejos.nxt.ColorSensor;
import lejos.nxt.I2CPort;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;

public class TestRotate {
	
	/**
	 * test the rotate method.
	 * just rotate the robot until u see the phase it takes, and than change wheelDistatnce to wheelDistance*(1 - phase) 
	 * @param args
	 */
	public static void main(String[] args) {
		
		double wheelDistance = 12.1, wheelDiameter = 5.6;
		DifferentialPilot pilot = new DifferentialPilot(Constants.wheelDiamet, Constants.wheelDist, Motor.C, Motor.A);
		int n = 0;
		Button.waitForAnyPress();
		while(Button.waitForAnyPress(2) != Button.ID_LEFT) {
			System.out.println(++n);
			pilot.rotate(90);
		}
	
	}
}
