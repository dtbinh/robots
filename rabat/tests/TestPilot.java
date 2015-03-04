package rabat.tests;

import rabat.Constants;
import rabat.alpha.DifferentialPilot;
import lejos.nxt.Button;
import lejos.nxt.Motor;

public class TestPilot {
	public static void main(String[] args) {
		DifferentialPilot pilot = new DifferentialPilot(Constants.wheelDiamet, Constants.wheelDist, Motor.C, Motor.A);
		int i=0;
		Button.waitForAnyPress();
		while(Button.waitForAnyPress(2000)!=Button.ID_LEFT){
			System.out.println(i++);
			pilot.rotate(90);
		}
		Button.waitForAnyPress();
	}
}
