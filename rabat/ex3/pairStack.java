package rabat.ex3;

import java.util.ArrayList;

import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;

class pairStack {
	ArrayList<Integer> X = new ArrayList<>();
	ArrayList<Integer> Y = new ArrayList<>();
	int n = 0;
	
	public pairStack push(int x, int y) {
//		System.out.println(n + " " + x +" " + y + " ip");
		X.add(n, x);
		Y.add(n, y);
		n++;
		return this;
	}
	
	public pairStack push(Pose pose) {
		return push((int)pose.getX(), (int)pose.getY());
	}
	
	public Waypoint peek() {
		return new Waypoint(X.get(n-1), Y.get(n-1));
	}
	
	public Waypoint pop(float heading) {
//		System.out.println(n);
		n--;
		return new Waypoint(X.get(n), Y.get(n), heading);			
	}
	
	public Waypoint pop() {
//		System.out.println(n);
		n--;
		return new Waypoint(X.get(n), Y.get(n));			
	}

	public boolean contains(int x, int y) {
		for(int i = 0; i < n; i++)
			if(X.get(i) == x && Y.get(i) == y)
				return true;
		return false;
	}
	
	public boolean isEmpty() {
		return n == 0;
	}
	
	public Path toPath() {
		Path path = new Path();
		for(int i = n - 1; i >= 0; i--) {
			path.add(new Waypoint(X.get(i), Y.get(i)));
		}
		return path;
	}

	public int size() {
		return n;
	}

	public boolean peek(int x, int y) {
		return X.get(n-1) == x && Y.get(n-1) == y;
	}

	public void push(Waypoint cur) {
		push((int)cur.getX(), (int)cur.getY());
		
	}
	
//	public pairStack push(float x, float y) {
//		return push((int)x, (int)y);
//		
//	}
	
}
