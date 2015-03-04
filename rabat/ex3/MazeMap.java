package rabat.ex3;


import rabat.Constants;
import rabat.Navigator;
import rabat.Utils;
import lejos.nxt.LCD;
import lejos.robotics.localization.PoseProvider;
import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;

public class MazeMap {
	private static int size = (int) Math.max(Constants.sizeX, Constants.sizeY), dSize = (int) (2*size - 1), cordDiff = (int) (size - 1);
	
	private int[][] map = new int[dSize][dSize];
	
	private static final int TEMP_DEFAULT = (int) (size*size);
	private int[][] temp = new int[dSize][dSize];
	
	//target X & Y, as absolute points
	private int targX = 0, targY = 0; 
	
	
	public MazeMap() {
		for(int i = 0; i < temp.length; i++)
			for(int j = 0; j < temp[i].length; j++)
				temp[i][j] = TEMP_DEFAULT;
	}
	
//	public MazeMap(int val) {
//		for(int i = 0; i < map.length; i++)
//			for(int j = 0; j < map[i].length; j++)
//				map[i][j] = val;
//	}
	
	//************** target methods
	public boolean isTarget(int x, int y) {
//		assert(x < size && x > -size && y > - size && y< size);
		return ((x == targX) && (y == targY));
	}
	
	public void setTarget(int x, int y) {
//		assert(x < size && x > -size && y > - size && y< size);
		targX = x;
		targY = y;
		addAttr(x, y, Constants.TARGET);
	}
	
	public Waypoint getTarget() {
		return new Waypoint(targX, targY);
	}
	
	
	//************** map methods
	public int get(int x, int y) {
//		assert(x < size && x > -size && y > - size && y< size);
//		if(x<0||y<0){
//			LCD.drawString(map[x + cordDiff][y + cordDiff]+":"+x+","+y,0,6);
//		}
		try{
			return map[x + cordDiff][y + cordDiff];
		}catch(Exception e){
			return 0;
		}
	}
	
	public int get(Waypoint p) {
		return (int) get((int) Math.round( p.getX()), (int) Math.round( p.getY()));
	}
	
	public void set(int x, int y, int val) {
//		assert(x < size && x > -size && y > - size && y< size);
		try{
			map[x + cordDiff][y + cordDiff] = val;
		}catch(Exception e){}
	}

	public void addAttr(int x, int y, int attr) {
		set(x, y, (int) (get(x, y)|attr));
	}
	
	public void addAttr(Waypoint loc, int stat) {
		set(loc, (get(loc)|stat));
		
	}

	public void setTarget(Waypoint loc) {
		setTarget((int) Math.round( loc.getX()), (int) Math.round( loc.getY()));
		
	}

	public void set(Waypoint loc, int stat) {
		set((int) Math.round( loc.getX()), (int) Math.round( loc.getY()), stat);
		
	}

	public void setAvaliable(int x, int y) {
//		assert(x < size && x > -size && y > - size && y< size);
		addAttr(x, y,  Constants.AVALIABLE);
	}

	public boolean shouldCheck(int x, int y) {
//		assert(x < size && x > -size && y > - size && y< size);
		int stat = get(x, y);
		return (((stat&Constants.AVALIABLE) != 0) && ((stat&Constants.VISITED) == 0));
	}
	
	private boolean check(int x, int y, int attr) {
//		assert(x < size && x > -size && y > - size && y< size);
		return (get(x, y)&attr) != 0;
	}
	
	public boolean check(Waypoint loc, int attr) {
		return check((int) Math.round( loc.getX()), (int) Math.round( loc.getY()), attr);
	}
	
	public boolean isAvaliable(int x, int y) {
		return check(x, y, Constants.AVALIABLE);
	}
	
	public boolean isVisited(int x, int y) {
		return check(x, y, Constants.VISITED);
	}
	
	public boolean isVisited(Waypoint waypoint) {
		return isVisited((int)waypoint.getX(), (int)waypoint.getY());
	}
	
	public boolean hasLeft(int x, int y) {
		return check(x, y, Constants.LEFT);
	}	
	
	public boolean hasRight(int x, int y) {
		return check(x, y, Constants.RIGHT);
	}
	
	public boolean hasBack(int x, int y) {
		return check(x, y, Constants.BACK);
	}
	
	public boolean hasForward(int x, int y) {
		return check(x, y, Constants.FORWARD);
	}
	
	//************ get start and end values
	public int getStartX() {
		for(int x = -cordDiff; x < map.length-cordDiff; x++)
			for(int y = -cordDiff; y < map[0].length-cordDiff; y++)
				if(isAvaliable(x, y))
					return x;
		return 0;
	}

	public int getStartY() {
		for(int y = -cordDiff; y < map.length-cordDiff; y++)
			for(int x = -cordDiff; x < map[0].length-cordDiff; x++)
				if(isAvaliable(x, y))
					return y;
		return 0;
	}
	
	public int getEndX() {
		for(int x = (int) map.length; x >= 0; x--)
			for(int y = 0; y < map[0].length; y++)
				if(isAvaliable(x, y))
					return x;
		return 0;
	}
	
	public int getEndY() {
		for(int y = (int) map.length; y >= 0; y--)
			for(int x = 0; x < map[0].length; x++)
				if(isAvaliable(x, y))
					return y;
		return 0;
	}
	
	public void resetPoseProv(PoseProvider prov) {
		prov.setPose(new Pose(0, 0, 0));
	}
	
//	private pairStack findPathRecursive(int startX, int startY, int endX, int endY, pairStack visited) {
////		System.out.println(startX +" " + startY);
//		if(visited.contains(endX, endY))
//			return visited;
//		if(hasRight(startX, startY) && tryPath(startX, (int) (startY - 1), endX, endY, visited))
//			return visited;
//		if(hasLeft(startX, startY) && tryPath(startX, (int) (startY + 1), endX, endY, visited))
//			return visited;
//		if(hasForward(startX, startY) && tryPath((int) (startX + 1), startY, endX, endY, visited))
//			return visited;
//		if(hasBack(startX, startY) && tryPath((int) (startX - 1), startY, endX, endY, visited))
//			return visited;
//		return null;
//	}
//	
//	private boolean tryPath(int startX, int startY, int endX, int endY, pairStack visited) {
//		Constants.debug(startX +" " + startY);
//		if(isAvaliable(startX, startY) && !visited.contains(startX, startY) &&
//				findPathRecursive(startX, startY, endX, endY, visited.push(startX, startY)) != null)
//			return true;
//		else {
////			System.out.println(startX +" " + startY + " is not avaliable or already in path");
//			visited.pop();
//		}
//		return false;
//		
//	}
	
	//*************** path finder
	
	private int getTemp(int x, int y) {
//		assert(x < size && x > -size && y > - size && y< size);
		try { 
			return temp[x + cordDiff][y + cordDiff];
		}catch(Exception e){
			return TEMP_DEFAULT;
		}
	}

	private int getTemp(Waypoint p) {
//		assert(x < size && x > -size && y > - size && y< size);
		return getTemp((int)p.getX(), (int)p.getY());
	}

	private void setTemp(int x, int y, int val) {
//		assert(x < size && x > -size && y > - size && y< size);
		try{
			temp[x + cordDiff][y + cordDiff] = val;
		}catch(Exception e) {
			Utils.debug("set temp error: "+x+","+y+":"+val);
			throw e;
		}
	}
	
	private void resetTemp(int startX, int startY) {
		pairStack stack = new pairStack();
		stack.push(startX, startY);
		while (!stack.isEmpty()) {
			Waypoint p = stack.pop();
			int x = (int) p.getX(), y = (int) p.getY();
			setTemp(x, y, TEMP_DEFAULT);
			if(getTemp(x + 1, y) != TEMP_DEFAULT)
				stack.push(x+1, y);
			if(getTemp(x - 1, y) != TEMP_DEFAULT)
				stack.push(x-1, y);
			if(getTemp(x, y+1) != TEMP_DEFAULT)
				stack.push(x, y+1);
			if(getTemp(x, y-1) != TEMP_DEFAULT)
				stack.push(x, y-1);
		}

	}
	
	public Path findpath(int startX, int startY, int endX, int endY) {
//		System.out.println(startX + " " + startY + " " + endX + " " + endY + " f");
//		Constants.debug("find path from " + startX +" " + startY +" to " + endX +" " + endY);
		try {
			return findPathRecursive(startX, startY, endX, endY);
		}catch(Exception e) {
			Utils.debug("find error " + startX + "," + startY + "," + endX + "," + endY + "," + e.getMessage());
			throw e;
		}
	}
	
	private Path findPathRecursive(int startX, int startY, int endX,
			int endY) {
		setTemp(startX, startY, 0);
		PairQueue queue = new PairQueue();
		queue.push(startX, startY);
		while (!queue.isEmpty()) {
			if(queue.peek(endX, endY))
				break;
			Waypoint p = queue.pop();
			int x= (int) p.getX(), y = (int) p.getY(), val = (int) (getTemp(x, y) + 1);
			if(hasBack(x, y) && getTemp(x - 1, y) > val) {
				queue.push(x - 1, y);
				setTemp(x - 1, y, val);
			}
			if(hasRight(x, y) && getTemp(x, y - 1) > val) {
				queue.push(x, y - 1);
				setTemp(x, y - 1, val);
			}
			if(hasForward(x, y) && getTemp(x + 1, y) > val) {
				queue.push(x + 1, y);
				setTemp(x + 1, y, val);
			}
			if(hasLeft(x, y) && getTemp(x, y + 1) > val) {
				queue.push(x, y + 1);
				setTemp(x, y+1, val);
			}
		}
		if(getTemp(endX, endY) == TEMP_DEFAULT)
			return null;
		pairStack stack = new pairStack();
		stack.push(endX, endY);
		Waypoint p;
		while (getTemp(p = stack.peek()) > 1) {
			int x = (int) p.getX(), y = (int) p.getY(), val = getTemp(p) - 1;
			if(getTemp(x - 1, y) == val && hasForward(x - 1, y)){
				stack.push(x - 1, y);
				continue;
			}
			if(getTemp(x + 1, y) == val && hasBack(x + 1, y)){
				stack.push(x + 1, y);
				continue;
			}
			if(getTemp(x, y-1) == val && hasLeft(x, y-1)){
				stack.push(x, y-1);
				continue;
			}
			if(getTemp(x, y+1) == val && hasRight(x, y+1)){
				stack.push(x, y+1);
				continue;
			}
			throw new IllegalStateException("error in building path");
		}
//		Constants.debug(getTemp(p) + " peek");
		stack.push(startX, startY);
		resetTemp(startX, startY);
		return stack.toPath();
	}

	public void go(Waypoint target, Navigator nav) {
		Pose cur = nav.getPoseProvider().getPose();
//		System.out.println(cur);
		Path path = findpath((int)Math.round(cur.getX()), (int)Math.round( cur.getY()),(int)Math.round(target.getX()), (int)Math.round( target.getY()));
//		Constants.debug("path found " + pose);
		nav.followPath(path);
	}
	
	public Path toTarget(Pose location) {
		return findpath((int)location.getX(), (int)location.getY(), targX, targY);
	}

}
