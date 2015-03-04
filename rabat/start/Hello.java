
package rabat.start;

import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
/**
 * move near the walls, using differential pilot
 * @author Ohad Cohen & Ron Cohen
 *
 */
public class Hello {
	
//	private static final int speed = 150;
	private static final int distance = 20;
	private static final int errorDist = 5;	

	
	public static void main(String[] args) {
		DifferentialPilot pilot = new DifferentialPilot(56, 121.5, Motor.C, Motor.B);
//		int dist;
		boolean stop = false;
		int rotatetion = 0;
		UltrasonicSensor SensF = new UltrasonicSensor(SensorPort.S3);
		UltrasonicSensor SensR = new UltrasonicSensor(SensorPort.S1);
		final int avgDistance = SensR.getDistance();
		final int maxDistance = avgDistance + errorDist;
		final int minDistance = avgDistance - errorDist;
		lejos.nxt.LCD.drawString("start!", 1, 1);
		pilot.setTravelSpeed(pilot.getTravelSpeed()*0.75);
		pilot.forward();
		
		while(!stop) {
			pilot.forward();
			if (SensF.getDistance()<distance) {
				pilot.stop();
				if(SensF.getDistance()<distance) {
					pilot.rotate(90);
					if(++rotatetion == 4)
						stop = true;
				}
			}
			
			if( SensR.getDistance() > maxDistance){
				pilot.steer(-20);
			}
			
			if( SensR.getDistance() < minDistance){
				pilot.steer(+20);
			}
//			if((dist=SensR.getDistance())>maxDistance || dist < minDistance) {
//				pilot.steer((dist-avgDistance)*10);
//				pilot.stop();
//				pilot.rotate(-30);
//				pilot.travel((dist-avgDistance)*2);
//				pilot.rotate(30);
//			}
//			if(dist<minDistance) {
//				pilot.stop();
//				pilot.rotate(30);
//				pilot.travel((avgDistance-dist)*2);
//				pilot.rotate(-30);
//			}
		}
	}
}
