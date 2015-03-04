package rabat.ex3;

import lejos.nxt.Button;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;

import java.util.Random;

import rabat.Constants;
import rabat.Utils;

public class MapTest {

	public static final int FORWARD = Constants.FORWARD;
	public static final int LEFT = Constants.LEFT;
	public static final int BACK = Constants.BACK;
	public static final int RIGHT = Constants.RIGHT;
	public static final int TARGET = Constants.TARGET;
	public static final int START = Constants.START;
	
	static int sizeX = 6, sizeY = 4, targX = 4, targY = 2;
	private static int[][] map = 
		{{FORWARD + START, FORWARD + BACK, FORWARD + LEFT + BACK, FORWARD + BACK, FORWARD + LEFT + BACK, BACK + LEFT},
		{FORWARD, BACK, LEFT + RIGHT, LEFT, RIGHT+ FORWARD, BACK + RIGHT + LEFT},
		{LEFT, LEFT, LEFT + RIGHT, RIGHT + LEFT + FORWARD, BACK + TARGET, RIGHT + LEFT},
		{RIGHT + FORWARD, RIGHT + FORWARD + BACK, BACK + RIGHT, RIGHT + FORWARD, FORWARD + BACK, BACK + RIGHT}};
	
	private static boolean consistencyTest(int[][] map) {
		boolean res = true;
		int x = 0,y = 0;
		try {
			if(map.length!=sizeY)
				res = false;
			for(y = 0; y < map.length; y++) {
				if(map[y].length != sizeX) {
					res = false;
					System.out.println("line " + y);
					continue;
				}
				for(x = 0; x < map[y].length; x++) {
					if(((map[y][x]&FORWARD) != 0) && ((map[y][x+1]&BACK) == 0)) {
						System.out.println(y + " " + x + "forward");
						res = false;
					}
					if(((map[y][x]&BACK) != 0) && ((map[y][x-1]&FORWARD) == 0)) {
						System.out.println(y + " " + x + "back");
						res = false;
					}
					if(((map[y][x]&RIGHT) != 0) && ((map[y-1][x]&LEFT) == 0)) {
						System.out.println(y + " " + x + "right");
						res = false;
					}
					if(((map[y][x]&LEFT) != 0) && ((map[y+1][x]&RIGHT) == 0)) {
						System.out.println(y + " " + x + "left");
						res = false;
					}
				}
			}
		}
		catch (Exception e) {
			System.out.println("error " + x + " " + y);
			Button.waitForAnyPress();
			throw e;
		}
		return res;
	}
	
	private static MazeMap initMaze() {
		MazeMap res = new MazeMap();
		
		int x = 0, y = 0;
		try {
			res.setTarget(targX, targY);
			for(y = 0; y < sizeY; y++)
				for(x = 0; x < sizeX; x++) {
					res.set(x, y, map[y][x] + Constants.AVALIABLE);
				}
		}catch (Exception e) {
			Utils.debug("cant init maze " + x + " " + y);
			throw e;
		}
		return res;
	}
	
	private static MazeMap initMaze2() {
		MazeMap res = new MazeMap();
		
		int x = 0, y = 0;
		try {
			res.setTarget(targX, targY);
			for(y = 0; y < sizeY; y++)
				for(x = 0; x < sizeX; x++) {
					res.set(y, x, map[y][x] + Constants.AVALIABLE);
				}
		}catch (Exception e) {
			Utils.debug("cant init maze " + x + " " + y);
			throw e;
		}
		return res;
	}
	
	private static boolean testDir(MazeMap maze) {
		boolean res = true;
		Path p;
		if((p = maze.findpath(0, 0, targX, targY)) == null )
			res = false;
		System.out.println("path to target " + p.size() + " long");
		if((p=maze.findpath(0, 0, 0, 0)) == null || p.size() > 2)
			res = false;
		else
			for(int i = 0; i < p.size(); i++)
				if(p.get(i).getX() != 0 || p.get(i).getY() != 0)
					res = false;
		return res;
	}
	
	private static boolean pushLocationTest(MazeMap map) {
		boolean res = true;
		int ret;
		Random rand = new Random(System.nanoTime());
		for (int i = 0; i < Constants.DIRECTIONS.length; i++) {
			if(MazeRobot.pushLocation(map, new pairStack(), new Waypoint(0, 0, rand.nextInt(20)), Constants.DIRECTIONS[i]) != 
					Constants.DIRECTIONS[i]){
				res = false;
				System.out.println(0 + " " + i);
			}
			if(MazeRobot.pushLocation(map, new pairStack(), new Waypoint(0, 0, -rand.nextInt(20)), Constants.DIRECTIONS[i]) !=
					Constants.DIRECTIONS[i]){
				res = false;
				System.out.println(0 + " " + i);
			}
			if((ret = MazeRobot.pushLocation(map, new pairStack(), new Waypoint(0, 0, 90 + rand.nextInt(20) - 10), Constants.DIRECTIONS[i])) != 
					MazeRobot.pushLocation(map, new pairStack(), new Waypoint(0, 0, -270 + rand.nextInt(20) + 10),Constants.DIRECTIONS[i])) {
				res = false;
				System.out.println(90 + " " + i + " " + ret);
			}
			if((ret = MazeRobot.pushLocation(map, new pairStack(), new Waypoint(0, 0, 180 + rand.nextInt(20) - 10), Constants.DIRECTIONS[i])) !=
					MazeRobot.pushLocation(map, new pairStack(), new Waypoint(0, 0, -180 + rand.nextInt(20) + 10),Constants.DIRECTIONS[i])) {
				res = false;
				System.out.println(180 + " " + i + " " + ret);
			}
			if((ret = MazeRobot.pushLocation(map, new pairStack(), new Waypoint(0, 0, 270 + rand.nextInt(20) - 10), Constants.DIRECTIONS[i])) !=
					MazeRobot.pushLocation(map, new pairStack(), new Waypoint(0, 0, -90 + rand.nextInt(20) + 10),Constants.DIRECTIONS[i])) {
				res = false;
				System.out.println(270 + " " + i + " " + ret);
			}
		}
//		System.out.println(MazeRobot.pushLocation(map, new pairStack(), new Waypoint(0, 0), Constants.LEFT));
		return res;
	}
	
	private boolean getDirectionTest() {
		boolean res = true;
		for(;;)
			break;
		return res;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		boolean res = true;
		if(consistencyTest(map))
			System.out.println("consistent");
		else {
			System.out.println("nor consistent");
			res = false;
		}
		MazeMap maze = initMaze();
		System.out.println("init success");
		if(testDir(maze))
			System.out.println("pased direction");
		else {
			System.out.println("failed direction test");
			res = false;
		}
		
		if(pushLocationTest(maze))
			System.out.println("pased push");
		else {
			System.out.println("failed push");
			res = false;
		}

		Button.waitForAnyPress();
		drawTest(initMaze());
		Button.waitForAnyPress();
		Utils.debug("finished tests");
	}

	private static void drawTest(MazeMap maze) {
		MazeRobot.drawMap(maze);
		Button.waitForAnyPress();
//		Constants.debug("press a key to print from another ditrection");
		MazeRobot.drawMap(initMaze2());
	}

}
