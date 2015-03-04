package rabat.ex2;

import lejos.nxt.Button;
import lejos.nxt.I2CPort;
import lejos.nxt.I2CSensor;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.nxt.addon.CompassHTSensor;
import lejos.nxt.addon.IRSeeker;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.localization.OdometryPoseProvider;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.mapping.NXTNavigationModel;
import lejos.robotics.navigation.CompassPilot;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Move;
import lejos.robotics.navigation.MoveListener;
import lejos.robotics.navigation.MoveProvider;
import lejos.robotics.navigation.Navigator;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.util.Delay;

class back {

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
			x = aPose.getX();
			y=aPose.getY();
			angle = compass.getDegreesCartesian()-iniAng;
			
		}
		
	}
	private static final int degErr = 10;
	private static final int gateAng = 0; 
	private static final int tblMid = 90;	
	private static float iniAng;
	private static int initX;
	
	private static CompassHTSensor compass = new CompassHTSensor(SensorPort.S2);
	
	private static void sleep(int time) {
		try {
			Thread.sleep(time);
			
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
		CompassHTSensor compass = new CompassHTSensor(SensorPort.S2);
		compass.getDegreesCartesian();
		UltrasonicSensor left = new UltrasonicSensor(SensorPort.S4);

		DifferentialPilot pilot = new DifferentialPilot(5.6, 18.35, Motor.A, Motor.C);
		
		pilot.setTravelSpeed(20);
		pilot.setRotateSpeed(70);
		MyProvider prov = new MyProvider(pilot);
//		CompassPilot co = new CompassPilot(compass, 5.6, 17.45, Motor.A, Motor.C);
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
		iniAng = compass.getDegreesCartesian();
		initX=left.getDistance();
		while(initX==255){
			initX = left.getDistance();
		}
		btReceiver.start();
		while (!btReceiver.isConnected());
		
		long time = System.nanoTime();
		ir.getDirection();
		pilot.reset();

		int goals=0;
		
//		navigator.rotateTo(90);
//		Button.waitForAnyPress();
//		pilot.rotate(90);
		
		while(goals <= 3){
			while(true){
				time = System.nanoTime();
				gotoBall(pilot, ir, navigator, ballSensor, ballMotor);
				kickBall(ballMotor, navigator,compass,left,pilot);
				sleep(2300);
				
				if(btReceiver.getMode()!= 0){
					goals++;	
					break;
				}
			}
//			navigator.getPoseProvider().setPose(getposition(compass,left,pilot));
			btReceiver.resetMode();
			System.out.println((System.nanoTime() - time)/((long)1E9) - 1);
//			navigator.getPoseProvider().setPose(getposition(compass, left, pilot));
			returnHome(pilot, navigator,compass);
		}
		
		btReceiver.stop();
		Button.waitForAnyPress();
	}

	private static Pose getposition(CompassHTSensor compass, UltrasonicSensor left, DifferentialPilot pilot) {
		double ang = -compass.getDegreesCartesian()+ iniAng;
//		ang = ang > 180? 180- ang:ang;
		ang = (ang+360)%360;
		double xAng = (ang>180)?360-ang:ang; 
		pilot.rotate(xAng);
		int sensDist = left.getDistance();
		while(sensDist==255){
			sensDist = left.getDistance();
		}
		float x = sensDist - initX;
		pilot.rotate(+90);
		sensDist = left.getDistance();
		while(sensDist==255){
			sensDist = left.getDistance();
		}
		float y = sensDist - 61;
		pilot.rotate(-90);
		return new Pose(x,y,compass.getDegreesCartesian() - iniAng);
	}
	
	private static void returnHome(DifferentialPilot pilot, Navigator navigator,CompassHTSensor compass) {
		LCD.drawString("home!", 2, 2);
		Pose p = navigator.getPoseProvider().getPose();
//		navigator.goTo(0,0,0);
		double x= p.getX(), y=p.getY(), ang = Math.atan2(y, x) - p.getHeading();
		double dist = Math.sqrt(x*x + y*y);
//		pilot.rotate(ang);
		navigator.rotateTo(Math.atan2(y, x));
		pilot.travel(-dist);
		navigator.rotateTo(0);
//		pilot.rotate(compass.getDegreesCartesian()-270 - iniAng);
		
	}

	private static boolean onBall(UltrasonicSensor ballSensor, RegulatedMotor ballMotor) {
		System.out.println(ballSensor.getDistance());
		if(ballSensor.getDistance() < 10) {
			ballMotor.rotate(90);
			sleep(100);
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

	private static void kickBall(RegulatedMotor ballMotor, Navigator navigator,CompassHTSensor compass,UltrasonicSensor left,DifferentialPilot pilot) {
		LCD.drawString("kick!", 2, 2);
		Pose p;
		navigator.getPoseProvider().setPose(getposition(compass,left,pilot));
		p=navigator.getPoseProvider().getPose();
		navigator.rotateTo(Math.atan2(160 - p.getY(),p.getX()));
		ballMotor.rotate(-90);
	}

}
