package rabat.ex3;

import java.util.ArrayList;

import lejos.robotics.navigation.Pose;
import lejos.robotics.navigation.Waypoint;
import lejos.robotics.pathfinding.Path;

public class PairQueue {
	ArrayList<Integer> X = new ArrayList<>();
	ArrayList<Integer> Y = new ArrayList<>();
	int n = 0, k = 0;
	
	public PairQueue push(int x, int y) {
//		System.out.println(n + " " + x +" " + y + " ip");
		X.add(n, x);
		Y.add(n, y);
		n++;
		return this;
	}
	
	public PairQueue push(Pose pose) {
		return push((int)pose.getX(), (int)pose.getY());
	}
	
	public Waypoint peek() {
		return new Waypoint(X.get(k), Y.get(k));
	}
	
	public Waypoint pop() {
		return new Waypoint(X.get(k), Y.get(k++));			
	}
	
	public boolean contains(int x, int y) {
		for(int i = k; i < n; i++)
			if(X.get(i) == x && Y.get(i) == y)
				return true;
		return false;
	}
	
	public boolean isEmpty() {
		return n == k;
	}
	
	public int size() {
		return n - k;
	}

	public boolean peek(int x, int y) {
		return X.get(k) == x && Y.get(k) == y;
	}

	public void push(Waypoint cur) {
		push((int)cur.getX(), (int)cur.getY());
		
	}
}
