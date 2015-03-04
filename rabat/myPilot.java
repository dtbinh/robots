package rabat;

import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.DifferentialPilot;

/**
 * a DifferentialPilot, that rotate reverse instead of more than 180 degrees 
 * @author robot
 *
 */
public class myPilot extends DifferentialPilot {

	public myPilot(double wheelDiameter, double trackWidth,
			RegulatedMotor leftMotor, RegulatedMotor rightMotor) {
		super(wheelDiameter, trackWidth, leftMotor, rightMotor);
	}
	
	@Override
	public void rotate(double angle, boolean immediateReturn) {
		angle+=360;
		angle%=360;
		if(angle>180)
			angle=angle-360;
		super.rotate(angle, immediateReturn);
	}

}
