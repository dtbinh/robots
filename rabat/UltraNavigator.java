package rabat;

import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.ArcRotateMoveController;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;

/**
 * this class represent a navigator, that keeps track of distance from walls
 * @author Ohad Cohen <ohadcn@cs.huji.ac.il>
 * @author Ron Cohen
 *
 */
public class UltraNavigator extends Navigator {

	UltrasonicSensor front, right;
	double block,dist;
	public UltraNavigator(ArcRotateMoveController pilot2, aPoseProvider poseProv, UltrasonicSensor front, UltrasonicSensor right,
			double blockSize, double dist) {
		super(pilot2, poseProv);
		this.front=front;
		this.right=right;
		this.block=blockSize;
		this.dist=dist;
	}

	@Override
	public void goTo(Waypoint waypoint, boolean canHead) {
		poseProv.setStraight();
		Pose p=poseProv.getPose();
		double ret,
			reverse=(Math.abs(p.angleTo(waypoint) - p.getHeading())==180?-1:1),
			ang=0;
		if((ret=Utils.readSonic(front))<block)
			pilot.travel(((ret%block-dist))/block);
		
		if((ret=Utils.readSonic(right))<block)
			pilot.rotate((ang=(dist-ret%block)*2*reverse));
		else if (ret==Constants.cutoffDist)
			pilot.rotate((ang=dist*2*reverse));
		
		poseProv.setPose(p);
		
		super.goTo(waypoint, canHead);
		pilot.rotate(-ang);
		poseProv.setStraight();
	}

}
