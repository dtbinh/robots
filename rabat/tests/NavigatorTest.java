package rabat.tests;
import java.io.IOException;

import rabat.Constants;
import rabat.Navigator;
import rabat.aPoseProvider;
import rabat.myPilot;
import lejos.nxt.Button;
import lejos.nxt.Motor;
import lejos.nxt.LCD;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.navigation.Pose;

/**
 * This program illustrates how to use the leJOS Navigator class to do waypoint navigation.
 *
 * Using waypoint navigation we can define a route that we want the robot to follow in terms
 * of the X and Y co-ordinates of waypoints, rather than in terms of turn and rotation commands.
 *
 * The program also illustrates how to:
 * (a) check when the robot is moving using the Navigator.isMoving() method
 * (b) get the current target waypoint (i.e., what is the next waypoint on the route that the robot is headin to) using the Navigator.getWaypoint() method
 * (c) use the OdometryPoseProvider to get the pose of the robot using the OdometryPoseProvider.getPose() method
 *
 * J. Kelleher 21022012
 * revised: Ohad Cohen <ohadcn@cs.huji.ac.il>
 */
public class NavigatorTest
{
	Navigator nav;

	public static void main(String[] args) throws IOException
	{
		
		//Setup the Navigation infrastructure
		myPilot pilot = new myPilot(
//				rabat.DifferentialPilot pilot = new rabat.DifferentialPilot(
//				lejos.robotics.navigation.DifferentialPilot pilot = new lejos.robotics.navigation.DifferentialPilot(
				Constants.wheelDiamet, Constants.wheelDist, Motor.C, Motor.A);
		aPoseProvider pp = new aPoseProvider(pilot);
		pilot.addMoveListener(pp);
		Navigator nav = new Navigator(pilot, pp);

		nav.addWaypoint(new Waypoint(30,0));
		nav.addWaypoint(new Waypoint(30, 30));
		nav.addWaypoint(new Waypoint(0,30));
		nav.addWaypoint(new Waypoint(0, 0, 0));

		System.out.println("Press any button to start");
		Button.waitForAnyPress();
		
		nav.followPath();

		//Display the current target waypoint as
		//the robot moves along the path
//		Waypoint wp;
//		while(nav.isMoving()) {
//		    wp = nav.getWaypoint();
//		    LCD.clear();
//		    System.out.println("Target WP:");
//		    System.out.println(wp);
//		}
		Button.waitForAnyPress();

		//When the robot stops moving 
		//display the final pose
		Pose ps = pp.getPose();
		LCD.clear();
		System.out.println("Final pose is:");
		System.out.println(ps);
		System.out.println("Any button to halt");

		Button.waitForAnyPress();
	}
}
