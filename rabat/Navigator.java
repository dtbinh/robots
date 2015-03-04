package rabat;

import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.ArcRotateMoveController;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;

/**
 * This class controls a robot to traverse a Path,  a sequence of  {@link  lejos.robotics.navigation.Waypoint}s.
 * It's default mode for a new path is continuous movement (no stopping at waypoints)  but see also {@link #singleStep(boolean)}.  To interrupt the path traversal,  call stop().
 *  It uses  an inner class running its own thread to issue movement commands to its
 * {@link lejos.robotics.navigation.MoveController},
 * which can be either a {@link rabat.myPilot} (recommended),
 *   {@link lejos.robotics.navigation.DifferentialPilot}
 * or {@link lejos.robotics.navigation.SteeringPilot}.
 * It also uses a {@link rabat.aPoseProvider}
 * 
 * @author Ohad Cohen <ohadcn@cs.huji.ac.il>
 * @author Ron Cohen
 */
public class Navigator {

	ArcRotateMoveController pilot;
	aPoseProvider poseProv;
	Path path;

	/**
	 * Allocates a Navigator object using a pilot and a custom poseProvider, rather than the default
	 * OdometryPoseProvider.  
	 * @param pilot  the pilot 
	 * @param aposeProvider  the custom PoseProvider
	 */
	public Navigator(ArcRotateMoveController pilot2, aPoseProvider poseProv) {
		this.pilot = pilot2;
		this.poseProv = poseProv;
		path = new Path();
	}

	@Deprecated
	public Navigator(ArcRotateMoveController pilot2) {
		this(pilot2, new aPoseProvider(pilot2));
	}

	/**
	 * Starts the robot traversing the path.
	 * @param path  to be followed.
	 */
	public void followPath(Path path) {
		for(int i=0;i<path.size()-1; i++)
			goTo(path.get(i),false);
		goTo(path.get(path.size()-1), true);

	}

	/**
	 * Returns the PoseProvider
	 * @return the PoseProvider
	 */
	public PoseProvider getPoseProvider() {
		return poseProv;
	}

	/**
	 * Starts the robot moving toward the destination.
	 * @param destination  the waypoint to be reached
	 * @param canHead whether to follow the heading in the {@link Waypoint} or not
	 */
	public void goTo(Waypoint waypoint, boolean canHead) {
		Pose p = poseProv.getPose();
		double ang = p.angleTo(waypoint) - p.getHeading();
		boolean reverse=Math.abs(Math.round(ang/90.0))==2 && !canHead;
		double dist = p.distanceTo(waypoint);
		if(dist>0 && !reverse)
			pilot.rotate(reverse?-ang:ang);
		pilot.travel(reverse?-dist:dist);
		if(canHead && waypoint.isHeadingRequired())
			pilot.rotate(waypoint.getHeading()-poseProv.getPose().getHeading());
	}

	/**
	 * Starts the robot moving toward the destination.
	 * @param destination  the waypoint to be reached
	 */
	public void goTo(Waypoint waypoint) {
		goTo(waypoint, false);
	}	

	/**
	 * returns the position of the robot.
	 * @return the position of the robot.
	 */
	public Pose getPose() {
		return poseProv.getPose();
	}

	/**
	 * Stops the robot. 
	 * The robot will resume its path traversal if you call {@link #followPath()}.
	 */
	public void stop()
	{
		pilot.stop();
	}

	/**
	 * Adds a  Waypoint  to the end of the path. 
	 * @param aWaypoint  to be added
	 */
	public void addWaypoint(Waypoint waypoint) {
		path.add(waypoint);

	}

	/**
	 * Constructs an new Waypoint from the parameters and adds it to the end of the path. 
	 * @param x coordinate of the waypoint
	 * @param y coordinate of the waypoint
	 */
	public void addWaypoint(float x, float y)
	{
		addWaypoint(new Waypoint(x, y));
	}

	/**
	 * Starts the robot traversing the current path. 
	 * This method is non-blocking; 
	 */
	public void followPath() {
		followPath(path);

	}

	/**
	 * Constructs an new Waypoint from the parameters and adds it to the end of the path. 
	 * @param x coordinate of the waypoint
	 * @param y coordinate of the waypoint
	 * @param heading the heading of the robot when it reaches the waypoint
	 */
	public void addWaypoint(float x, float y, float heading)
	{
		addWaypoint(new Waypoint(x, y, heading));
	}

	public boolean isMoving() {
		return pilot.isMoving();
	}

	/**
	 * Starts the  moving toward the destination Waypoint created from 
	 * the parameters. 
	 * @param x  coordinate of the destination
	 * @param y  coordinate of the destination
	 */
	public void goTo(float x, float y)
	{
		goTo(new Waypoint(x, y));
	}

	/**
	 * Starts the  moving toward the destination Waypoint created from 
	 * the parameters. 
	 * @param x coordinate of the destination
	 * @param y coordinate of th destination
	 * @param heading  desired robot heading at arrival 
	 */
	public void goTo(float x, float y, float heading)
	{
		goTo(new Waypoint(x, y, heading));
	}

}
