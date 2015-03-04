package rabat;

import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.MoveProvider;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;

/**
 * a pose provider, for keeping track of robot position, by following a {@link MoveListener}
 * (e,g: {@link DifferentialPilot}, or {@link myPilot}).
 * @author Ohad Cohen <ohadcn@cs.huji.ac.il>
 * @author Ron Cohen
 *
 */
public class aPoseProvider implements PoseProvider, MoveListener {

		double x = 0, y = 0, angle = 0;
		public aPoseProvider(MoveProvider pilot) {
			pilot.addMoveListener(this);
		}

		@Override
		public void moveStarted(Move event, MoveProvider mp) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void moveStopped(Move event, MoveProvider mp) {
			double dist = event.getDistanceTraveled();
			angle += event.getAngleTurned();
			angle+=360;
			angle%=360;
			x += dist * Math.cos(angle*Math.PI/180.0);
			y += dist * Math.sin(angle*Math.PI/180.0);
		}

		@Override
		public Pose getPose() {
			return new Pose(Math.round(x), Math.round(y), (float) angle);
		}

		@Override
		public void setPose(Pose aPose) {
			x = aPose.getX();
			y = aPose.getY();
			angle = aPose.getHeading();
			aPose.getLocation();
			
		}
		
		/**
		 * returns the X coordinate of the robot position.
		 * @return the X coordinate of the robot position
		 */
		public long getX() {
			return Math.round(x);
		}
		
		/**
		 * returns the Y coordinate of the robot position.
		 * @return the Y coordinate of the robot position
		 */
		public long getY() {
			return Math.round(y);
		}
		
		/**
		 * returns the heading of the robot.
		 * @return the heading of the robot 
		 */
		public float getHeading() {
			return (float) angle;
		}

		/**
		 * round the position of the robot.
		 * X and Y are rounds to the nearest integer, the heading is rounded to 90 degrees. 
		 */
		public void setStraight() {
			x=Math.round(x);
			y=Math.round(y);
			angle=90*Math.round(angle/90.0);
			
		}

		/**
		 * returns a {@link Waypoint}, represent the robot position and heading.
		 * this waypoint can be used by a {@link Navigator} to get back to this point.
		 * @return a {@link Waypoint}, represent the robot position.
		 */
		public Waypoint getWaypoint() {
			return new Waypoint(x, y, angle);
		}
	}
