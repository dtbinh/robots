package rabat.ex1;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.RegulatedMotor;
import lejos.robotics.navigation.CompassPilot;
import lejos.robotics.navigation.OmniPilot;
import lejos.nxt.addon.CompassHTSensor;

import java.util.Timer;

public class CenterDot {
	private static RegulatedMotor motorL = Motor.A;
	private static RegulatedMotor motorR = Motor.C;
	private static final int maxSpeed =(int) motorL.getMaxSpeed(); 
	
	private static CompassHTSensor compass = new CompassHTSensor(SensorPort.S2);
	
	private static double wheelDiameter = 56;
	private static double wheelDist = 171;
	private static double wheelScope = wheelDiameter * Math.PI;
	private static double rotateAngle = (wheelDist/(wheelDiameter));
	
	private static LightSensor sensC = new LightSensor(SensorPort.S3);
	
	private static int fixFactor = 10;

	private static UltrasonicSensor sensBack = new UltrasonicSensor(SensorPort.S4);
	private static UltrasonicSensor sensFwd = new UltrasonicSensor(SensorPort.S1);
	
	private static int avgLight = (sensC.getHigh() - sensC.getLow())/2;
	private static int errLight = avgLight / 10;
	private static int minLight = sensC.getLow() + errLight;
	private static int maxLight = sensC.getHigh() - errLight;
	
	private static long startTime;
	private static long times = 0;

	
	private static void finish() {
		stop();
		System.out.println(Math.floor((System.nanoTime() - startTime)/1E7)/1E2 + " seconds");
		System.out.println("it took " + times);
		Button.waitForAnyPress();
		System.out.println("finish!");
		System.exit(0);
	}

	private static void forward(double mm) {
		int move = (int) (360*mm/(wheelScope));
		onDot();
		try {
			motorL.rotate(move, true);
			motorR.rotate(move);
		}
		catch (Exception e) {
			System.out.println("stoped");
		}
		onDot();
	}
	
	private static void stop() {
		try {
			motorL.stop(true);
			motorR.stop();
		}catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
	}
	
	private static void turn(double degrees) {
		int angle = (int) (rotateAngle * degrees);
		angle = angle > 180? 180 - angle : angle;
		onDot();
		try {
			motorL.rotate(angle, true);
			motorR.rotate(-angle);
		}catch(Exception e) {
			System.out.println(e.getMessage());
		}
		onDot();
	}
	
	private static int readDist(UltrasonicSensor sens) {
		int dist;
		while((dist = sens.getDistance()) > 250)
			System.out.println("read error");
		return dist;
	}
	
	private static void toCenter() {
		int dist;
		System.out.println("Center");
		while(Math.abs(dist = (readDist(sensFwd) - readDist(sensBack))) > 5 ||
				Math.abs(dist = (readDist(sensFwd) - readDist(sensBack))) > 5){
			forward(dist*3);
		}
		stop();
		System.out.println(readDist(sensBack) + " " + readDist(sensFwd));
	}
		
	private static void northen() {
		while(compass.getDegreesCartesian() > 10 && compass.getDegreesCartesian() < 350) {
			turn( compass.getDegreesCartesian());
			System.out.println(compass.getDegreesCartesian());
		}
	}
	
	private static void center2D() {
		System.out.println("center2D");
		northen();
		toCenter();
		turn((compass.getDegreesCartesian() + 90)%360);
		toCenter();
		
	}
		
	private static boolean onDot() {
		if(sensC.getNormalizedLightValue() < avgLight)
			finish();
		return false;
	}
	
	private static void toDot() {
		final int startDist = - 10;
		final int n = 6;
		toCenter();
		final int angle = 5; 
		int dist = startDist;
		System.out.println("find the dot");
		forward(-90);
		while(! onDot()) {
			for(int i = 0; i < n && !onDot(); i++){
				turn(angle);
			}
			if(!onDot()) {
				turn(-angle*n);
				for(int i = 0; i < n && !onDot(); i++){
					turn(-angle);
				}
			}
			
			if(!onDot()) {
				turn(angle*n);
				forward(dist);
				dist *= - 1.2;
				if(dist > 20) {
					center2D();
					forward(-70);
				}
			}
		}
		stop();
	}
	
	public static void main(String[] args) throws InterruptedException {

		//force compass to initialize itself
		compass.getDegreesCartesian(); 
		
		Button.waitForAnyPress();
		Thread.sleep((long) 2E3);
		Thread t = new Thread(new test());
		t.start();
		startTime =  System.nanoTime();
		try {
			center2D();
			toDot();
		}catch (Exception e) {
			System.out.println(e.getLocalizedMessage());
		}
		return;
	}

	
	private static class test implements Runnable {
		@Override
		public void run() {
			while(true) {
				try {
					onDot();
				}catch (Exception e) {
					System.out.println(e.getLocalizedMessage());
				}
				times ++;
			}
		}
		
	}
}
