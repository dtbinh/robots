package rabat.ex2;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.IRSeeker;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.MoveProvider;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;

class Soccer {

	static class MyProvider implements PoseProvider, MoveListener {

		float x = 0, y = 0, angle = 0;
		public MyProvider(DifferentialPilot pilot) {
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
			x += dist * Math.cos(angle*Math.PI/180.0);
			y += dist * Math.sin(angle*Math.PI/180.0);
		}

		@Override
		public Pose getPose() {
			return new Pose(x, y, angle);
//			return null;
		}

		@Override
		public void setPose(Pose aPose) {
			// TODO Auto-generated method stub
			
		}
		
	}

	private static void sleep() {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	static public int lightFlow(IRSeeker ir, int dir) {
		return (ir.getSensorValue((int)Math.ceil(dir/2.0)) + 
				((dir%2)==1?0:ir.getSensorValue((int)Math.ceil(dir/2.0)+1)))/((dir%2)==1?1:2);
	}

	static BTReceiver btReceiver = new BTReceiver();
	@SuppressWarnings("deprecation")
	public static void main(String[] args) {
		IRSeeker ir = new IRSeeker(SensorPort.S1);
		ir.setAddress(0x10);

		DifferentialPilot pilot = new DifferentialPilot(5.6, 17.45, Motor.A, Motor.C);
		pilot.setTravelSpeed(20);
		MyProvider prov = new MyProvider(pilot);
		
//		OdometryPoseProvider prov = new OdometryPoseProvider(pilot);
//		pilot.addMoveListener(prov);
//		prov.setPose(new Pose());
		Navigator navigator = new Navigator(pilot, prov);
//		NXTNavigationModel nav = new NXTNavigationModel();
//		nav.addNavigator(navigator);
//		navigator.addNavigationListener(nav);

		RegulatedMotor ballMotor = Motor.B;
		ballMotor.setSpeed((int)( ballMotor.getMaxSpeed()));
		UltrasonicSensor ballSensor = new UltrasonicSensor(SensorPort.S3);

		Button.waitForAnyPress();
		
//		btReceiver.start();
//		while (!btReceiver.isConnected());
		
		long time = System.nanoTime();
		ir.getDirection();
		pilot.reset();

		int goals=0;
		
		
		while(goals <= 3){
//			while(true){
				time = System.nanoTime();
				gotoBall(pilot, ir, navigator, ballSensor, ballMotor);
				kickBall(ballMotor, navigator);
				sleep();
//				if(btReceiver.getMode()!=0){
//					goals++;	
//					break;
//				}
//			}
			
//			btReceiver.resetMode();
			System.out.println((System.nanoTime() - time)/((long)1E9) - 1);
			returnHome(pilot, navigator);
		}
		
//		btReceiver.stop();
		Button.waitForAnyPress();
	}

	private static void returnHome(DifferentialPilot pilot, Navigator navigator) {
		LCD.drawString("home!", 2, 2);
		Pose p = navigator.getPoseProvider().getPose();
//		navigator.goTo(0,0,0);
		double x= p.getX(), y=p.getY(), ang = Math.atan2(y, x)*180.0/Math.PI - p.getHeading();
		double dist = Math.sqrt(x*x + y*y);
		pilot.rotate(ang);
		pilot.travel(-dist);
		pilot.rotate(-navigator.getPoseProvider().getPose().getHeading());
	}

	private static boolean onBall(UltrasonicSensor ballSensor, RegulatedMotor ballMotor) {
		System.out.println(ballSensor.getDistance());
		if(ballSensor.getDistance() < 10) {
			ballMotor.rotate(90);
			sleep();
//			if(ballSensor.getDistance() > 10 && ballSensor.getDistance() > 10) {
//				ballMotor.rotate(-90);
//				return false;
//			}
			return true;
		}
		return false;
	}

	private static void gotoBall(DifferentialPilot pilot, IRSeeker ir, Navigator navigator,
			UltrasonicSensor ballSensor, RegulatedMotor ballMotor) {
		LCD.drawString("goto ball!", 2, 2);
		int dir = ir.getDirection();

//		int rot = (5-dir) * 20;
		if(dir != 0)
//			navigator.rotateTo((5 - dir) * (20));
			pilot.rotate((5 - dir) * (20));

		while(!onBall(ballSensor, ballMotor)) {
			dir = ir.getDirection();
			if(dir == 0 && ir.getDirection() == 0) {
//				navigator.stop();
				pilot.stop();
//				navigator.rotateTo(navigator.getPoseProvider().getPose().getHeading() +  5);
				pilot.rotate(5);
			}
			else {
				pilot.forward();
				pilot.steer((5-dir) *40);
//				navigator.getMoveController().
//				if(dir != 5)
//					navigator.rotateTo(navigator.getPoseProvider().getPose().getHeading() + (5-dir) * 10);
//				navigator.getMoveController().forward();
			}
//			System.out.println("steer " + dir + " " + rot );
		}
		pilot.stop();
	}

	private static void kickBall(RegulatedMotor ballMotor, Navigator navigator) {
		LCD.drawString("kick!", 2, 2);
		Pose p = navigator.getPoseProvider().getPose();
		navigator.rotateTo(Math.atan2(p.getY(),150 -  p.getX()));
		ballMotor.rotate(-90);
	}

}
