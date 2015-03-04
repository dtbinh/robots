package rabat.ex3;

import java.util.HashMap.Entry;
import java.util.Stack;

import lejos.nxt.ColorSensor;
import lejos.nxt.LightSensor;
import lejos.nxt.Motor;
import lejos.nxt.SensorPort;
import lejos.nxt.UltrasonicSensor;
import lejos.robotics.navigation.DifferentialPilot;
import lejos.robotics.navigation.Navigator;

/**
 * just a draft, not used
 * @author Ohad Cohen <ohadcn@cs.huji.ac.il>
 *
 */
public class Maze {
	Stack<Entry<Long, Long>> unVisited = new Stack<>();
	private static final int distance = 20;
	private static final int errorDist = 5;	

	private static final int SIZEX = 6;
	private static final int SIZEY = 4;
	
	private static final int FORWARD = 1;
	private static final int LEFT = 2;
	private static final int BACK = 4;
	private static final int RIGHT = 8;
	private static final int AVALIABLE = 16;
	private static final int VISITED = 32;

	private static int[] map = new int[SIZEX*SIZEY];
	
	private static int nightbor(int pose, int direction) {
		switch (direction) {
		case FORWARD:
			return pose + 1;
		case BACK:
			return pose - 1;
		case RIGHT:
			return 12;
		default:
			return 0;
		}
	}
	
	private static int[] findPath(int[] stat, int start, int end) {
		int[] path = new int[SIZEX * SIZEY];
		Stack<Integer> stack = new Stack<>();
		while(start != end) {
			//TODO
		}
		return path;
	}
	
	private static final int gotoTarget(int[] map, int start, int end, DifferentialPilot pilot, int direction) {
		int[] path = findPath(map, start, end);
		for(int i = 0; i< path.length;i++) {
			pilot.rotate((direction - path[i])*90);
			direction = path[i];
			pilot.travel(30);
		}
		return direction;
	}
	public static void main(String[] args) {
		UltrasonicSensor sensR = new UltrasonicSensor(SensorPort.S1);
		UltrasonicSensor sensF = new UltrasonicSensor(SensorPort.S2);
		UltrasonicSensor sensL = new UltrasonicSensor(SensorPort.S4);
		ColorSensor light = new ColorSensor(SensorPort.S3);
		DifferentialPilot pilot = new DifferentialPilot(56, 121.5, Motor.C, Motor.A);
//		navigator.
		
		map[0] = AVALIABLE;
		int location = 0, direction = FORWARD, tar = 0;
		Stack<Integer> stack = new Stack<>();
		stack.push(0); //zero is the startpoint
		do {
			int next = stack.pop();
			direction = gotoTarget(map, location, next, pilot, direction);
			location = next;
			if(sensF.getDistance() > 30) {
				map[location] += direction;
//				if()
			}
			if(sensL.getDistance() > 30) {
				map[location] += (direction == RIGHT? FORWARD:direction * 2);
			}
			if(sensR.getDistance() > 30) {
				map[location] += (direction == FORWARD? RIGHT:direction/2);
			}
			map[location] += VISITED;
			if(light.getLightValue() < 500)
				tar = location;
		} while(!stack.isEmpty());
		direction = gotoTarget(map, location, 0, pilot, direction);
		gotoTarget(map, 0, tar, pilot, direction);
	}

}
