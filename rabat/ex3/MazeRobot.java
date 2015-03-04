package rabat.ex3;

import rabat.*;
import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.robotics.navigation.Waypoint;

public class MazeRobot{
	public static myPilot pilot = new myPilot(
//	public static rabat.DifferentialPilot pilot = new rabat.DifferentialPilot(
//	public static lejos.robotics.navigation.DifferentialPilot pilot = new lejos.robotics.navigation.DifferentialPilot(
			Constants.wheelDiamet/Constants.blockSize,
			Constants.wheelDist/Constants.blockSize, Motor.C, Motor.A);
	
	public static aPoseProvider poseProv = new aPoseProvider(pilot);
	
	public static UltraNavigator navigator = new UltraNavigator(pilot, poseProv, Constants.sensF,Constants.sensR,Constants.blockSize,Constants.dist);
	
	public static MazeMap map = new MazeMap();
	
	public static int X = 0, Y = 0;
	private static long startTime = System.nanoTime();

	
	public static void drawMap(MazeMap map) {
		LCD.clear();
		int endX = map.getEndX(), endY = map.getEndY(),
				startX = map.getStartX(), startY = map.getStartY(),
				diffX = endX - startX + 1, diffY = endY - startY + 1;
		boolean reverse=diffX>diffY;
		int	pixX = (reverse?Constants.boardDrawWidth:Constants.boardDrawHeight)/diffX,
				pixY =(reverse?Constants.boardDrawHeight:Constants.boardDrawWidth)/diffY;
		for(int x=startX; x <= endX; x++)
			for(int y=startY; y <= endY; y++) {
				drawPoint(reverse?endX-x:y-startY,reverse?y-startY:endX-x,
						reverse?pixX:pixY,reverse?pixY:pixX,
						reverse?map.get(x, y):(Constants.reverseTable[map.get(x, y)&0x0f]|map.get(x, y)&0xf0));
//				if(!reverse)
//					Button.waitForAnyPress();
//				drawPoint(endX-x,y-startY,pixX,pixY, map.get(x, y));
			}
	}
	
	private static void drawPoint(int x, int y, int pixX, int pixY, int stat) {
		int startX = x*(pixX) + 1, endX = (x + 1)*(pixX) - 1, 
				startY = y*(pixY) + 1, endY = (y + 1)*(pixY) - 1;
		for(int i = startX; i <= endX; i++)
			for(int j = startY; j <= endY; j++)
				if((stat & Constants.VISITED) != 0 && !(((stat&Constants.TARGET) != 0) && ((i-startX == j-startY)||(endX-i==j-startY))))
					LCD.setPixel(i, j, 0);
				else if((stat&Constants.AVALIABLE)!=0) {
					if((i+j)%2==1)
						LCD.setPixel(i, j, 0);
					else
						LCD.setPixel(i, j, 1);
				} else
					LCD.setPixel(i, j, 1);
//		LCD.drawString(x+","+y+":"+Integer.toHexString( stat),0,6);
		if((stat & Constants.START)!=0){
			LCD.setPixel((startX + endX)/2, (startY + endY) / 2, 1);
			LCD.setPixel((startX + endX)/2+1, (startY + endY) / 2, 1);
			LCD.setPixel((startX + endX)/2, (startY + endY) / 2+1, 1);
			LCD.setPixel((startX + endX)/2+1, (startY + endY) / 2+1, 1);
			
		}
		
		for(int i = startX; i < endX + 1; i++) {
			if((stat&Constants.LEFT)!=0)
				LCD.setPixel(i, endY + 1, 0);
			else
				LCD.setPixel(i, endY + 1, 1);

			if((stat&Constants.RIGHT)!=0)
				LCD.setPixel(i, startY - 1, 0);
			else
				LCD.setPixel(i, startY - 1, 1);

		}
			
		for(int i = startY; i < endY + 1; i++) {
			if((stat&Constants.BACK)!=0)
				LCD.setPixel(endX + 1, i, 0);
			else
				LCD.setPixel(endX + 1, i, 1);

			if((stat&Constants.FORWARD)!=0)
				LCD.setPixel(startX - 1, i, 0);
			else
				LCD.setPixel(startX - 1, i, 1);

		}
	
	}

	private static int getDirection(double d) {
		return (int) 1<<(Math.round(((d)/90.0)+4)%4);
	}
	
	public static int pushLocation(MazeMap map, pairStack stack, Waypoint loc, int sens) {
		int dir = (getDirection(loc.getHeading())*sens);
		dir = ((dir | (dir >> 4))&0x0f);
		assert(((dir)&(dir-1)) == 0);
		int x = (int) Math.round(loc.getX()), y = (int)Math.round(loc.getY());
		switch (dir) {
		case Constants.FORWARD:
			x++; 
			break;
		case Constants.RIGHT:
			y--;
			break;
		case Constants.LEFT:
			y++;
			break;
		case Constants.BACK:
			x--; 
			break;
			
		default:
			Utils.debug(dir + ":" +  loc.getHeading()+ "+"+ sens + " " + getDirection(loc.getHeading())+ " error in pushLocation");
		}
		if(map.isVisited(x, y))
			return dir | Constants.VISITED;

		stack.push(x, y);
		map.setAvaliable(x, y);
		return dir;
	}
	
	private static void pushNeighbors(pairStack stack, Waypoint loc) {
		int stat = 0;
		double ret;
				
		if(!map.check(loc, Constants.START)) {
			stat |= pushLocation(map, stack, loc, Constants.BACK);
			if(stat < Constants.VISITED)
				Utils.debug("back not visited " + Utils.dbl2Str(loc.getHeading()) + " " +  (int)getDirection(loc.getHeading()));
		}
		
		if((ret = Utils.readSonic( Constants.sensL)) > Constants.blockSize && ret < Constants.cutoffDist) {
			stat |= pushLocation(map, stack, loc, Constants.LEFT);
		}
		
		if((ret = Utils.readSonic( Constants.sensF)) > Constants.blockSize && ret < Constants.cutoffDist) {
			stat |= pushLocation(map, stack, loc, Constants.FORWARD);
		}
			
		if((ret = Utils.readSonic( Constants.sensR)) > Constants.blockSize && ret < Constants.cutoffDist) {
			stat |= pushLocation(map, stack, loc, Constants.RIGHT);
		}
		
		if(Constants.light.getLightValue() < Constants.cutoffLight) {
			map.setTarget(loc);
			stat |= Constants.TARGET;
		}
		
		map.addAttr(loc, stat | Constants.VISITED);
		drawMap(map);
		LCD.drawString("time:"+(int)Math.floor((System.nanoTime() -  startTime)/1E9)+"s", 0, 6);
	}
	
	private static void mapMaze() throws Exception {
		map.resetPoseProv(poseProv);
		pairStack stack = new pairStack();
		Waypoint cur = new Waypoint(0, 0);
		map.set(cur, Constants.START|Constants.AVALIABLE);
		stack.push(cur);
		try {
			do {
				while(map.isVisited((cur = stack.pop()))) {
					if(stack.isEmpty()){
						return;					
					}
				}
				map.go(cur, navigator);
				pushNeighbors(stack, poseProv.getWaypoint());
			}while(!stack.isEmpty());
		
		}
		catch(Exception e) {
			Utils.debug("mapMaze error " + cur);
			throw e;
		}
	}
	
	private static void toTarget() {
		navigator.followPath(map.toTarget(poseProv.getPose()));
	}
	
	public static void main(String[] args) throws Exception {
		
		int time;
		pilot.setRotateSpeed(90.0);
		pilot.setTravelSpeed(.5);
		System.out.println("Press any key to start");
		Button.waitForAnyPress();

		mapMaze();
		LCD.drawString("mapped in " + Math.floor((System.nanoTime() -  startTime)/1E9), 0, 6);
		LCD.drawString("time:"+(time=(int)Math.floor((System.nanoTime() -  startTime)/1E9))+"s", 0, 6);
		
		map.go(new Waypoint(0, 0, 0), navigator);

		startTime = System.nanoTime();		
		
		toTarget();
		drawMap(map);
		LCD.drawString("mapping:"+time+"s, back:"+ Math.floor((System.nanoTime() -  startTime)/1E9), 10, 6);
		Button.waitForAnyPress();
	}
	
}
