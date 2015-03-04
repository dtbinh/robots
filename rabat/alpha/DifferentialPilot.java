package rabat.alpha;

import rabat.myPilot;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.MoveProvider;
import lejos.robotics.navigation.RotateMoveController;

/***
 * a DifferentialPilot equivalent.
 * 
 * small bugs left:
 * A. contacting MoveListner on asynchronous systems 
 * (needs to implement RegulatedMotorListener for this)
 * B. rotateSpeed and travelSpeed are the same
 * C. All getSpeed() and getMaxSpeed variants returns the left motor speed
 * D. implement steer
 * @deprecated use {@link lejos.robotics.navigation.DifferentialPilot} or {@link myPilot} instead
 * 
 * @author Ohad Cohen <ohadcn@cs.huji.ac.il>
 * @author Ron Cohen
 */
@Deprecated
public class DifferentialPilot implements MoveProvider, RotateMoveController {

	double wheelDiameter, wheelDistatnce, wheelScope, rotateAngle;
	PoseProvider prov;
	RegulatedMotor leftMotor, rightMotor;
	MoveListener poseProvider = null;
	Move move = null;
	boolean turn=true;

  /**
   * Allocates a DifferentialPilot object, and sets the physical parameters of the
   * NXT robot.<br>
   * make sure wheelDiameter & wheelDistatnce are in the same units, this unit 
   * will be used by {@link #travel()} method. <br>
   *
   * @param wheelDiameter
   *            Diameter of the tire, in any convenient units (diameter in mm
   *            is usually printed on the tire).
   * @param trackWidth
   *            Distance between center of right tire and center of left tire,
   *            in same units as wheelDiameter.
   * @param leftMotor
   *            The left Motor (e.g., Motor.C).
   * @param rightMotor
   *            The right Motor (e.g., Motor.A).
   */
	public DifferentialPilot(double wheelDiameter, double wheelDistatnce, final RegulatedMotor leftMotor,
			final RegulatedMotor rightMotor) {
		this.wheelDiameter = wheelDiameter;
		this.wheelDistatnce = wheelDistatnce;
		this.leftMotor = leftMotor;
		this.rightMotor = rightMotor;
		wheelScope = wheelDiameter * Math.PI / 360.0;
		rotateAngle = (wheelDistatnce/(wheelDiameter));
	}

	@Override
	public void rotate(double degrees) {
		rotate(degrees, false);
		
	}

	@Override
	public void travel(double distance) {
		travel(distance, false);
	}

	@Override
	public void addMoveListener(MoveListener aPoseProvider) {
		this.poseProvider = aPoseProvider;
		
	}

	@Override
	public void rotate(double degrees, boolean immidiateReturn) {
		degrees+=360;
		degrees%=360;
		degrees = degrees>180?degrees-360:degrees;
		if(poseProvider != null) {
			move = new Move(0, (float) degrees, false);
			poseProvider.moveStarted(move, this);
		}

		int angle = (int) (rotateAngle * degrees);
		try {
			leftMotor.rotate(-angle, true);
			rightMotor.rotate(angle, immidiateReturn);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
		if(poseProvider != null)
			poseProvider.moveStopped(move, this);
		
	}

	@Override
	public Move getMovement() {
		return move;
	}

	@Override
	public void forward() {
		leftMotor.forward();
		rightMotor.forward();
		
	}

	@Override
	public void backward() {
		leftMotor.backward();
		rightMotor.backward();
		
	}

	@Override
	public void stop() {
		leftMotor.stop();
		rightMotor.stop();
	}

	@Override
	public boolean isMoving() {
		return leftMotor.isMoving() || rightMotor.isMoving();
	}

	@Override
	public void travel(double distance, boolean immediateReturn) {
		if(poseProvider != null) {
			move = new Move((float) distance, 0, false);
			poseProvider.moveStarted(move, this);
		}
		
		try {
			int rotation = (int) (distance/wheelScope);
			(turn?leftMotor:rightMotor).rotate(rotation, true);
			(!turn?leftMotor:rightMotor).rotate(rotation, immediateReturn);
			turn=!turn;
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}

		if(poseProvider != null)
				poseProvider.moveStopped(move, this);
		
	}

	@Override
	public void setTravelSpeed(double speed) {
		leftMotor.setSpeed((int) speed);
		rightMotor.setSpeed((int) speed);
		
	}

	@Override
	public double getTravelSpeed() {
		return leftMotor.getSpeed();
	}

	@Override
	public double getMaxTravelSpeed() {
		return Math.min(leftMotor.getMaxSpeed(), rightMotor.getMaxSpeed());
	}


	@Override
	public void setRotateSpeed(double speed) {
		leftMotor.setSpeed((int) speed);
		rightMotor.setSpeed((int) speed);
	}


	@Override
	public double getRotateSpeed() {
		return leftMotor.getSpeed();
	}


	@Override
	public double getRotateMaxSpeed() {
		return Math.min(leftMotor.getMaxSpeed(), rightMotor.getMaxSpeed());
	}

}
