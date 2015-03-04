package rabat;

import lejos.nxt.Button;
import lejos.nxt.UltrasonicSensor;

/**
 * some useful methods
 * @author Ohad Cohen <ohadcn@cs.huji.ac.il>
 * @author Ron Cohen
 *
 */
public class Utils {
	public static final double precision = 1000;
	public static final int cutoffDist = 250;

	public static String dbl2Str(double d) {
		return " " + (Math.round(d*precision)/precision) + " ";
	}

	/**
	 * show a message, using stdout, and (optionally) wait for for a press
	 * @param s message to show
	 * @param press whether to wait for a press or not
	 * @return the button pressed
	 */
	public static int debug(String s, boolean press){
		System.out.println(s);
		int ret=0;
		if (press && (ret=Button.waitForAnyPress()) == Button.ID_LEFT)
			System.exit(0);
		return ret;
	}
	
	/**
	 * show a message, using stdout, and wait for for a press
	 * @param s message to show
	 * @param press whether to wait for a press or not
	 * @return the button pressed
	 */
	public static int debug(String s) {
		return debug(s, true);
		
	}
	
	/**
	 * returns the average reading of {@link lejos.nxt.UltrasonicSensor}.
	 * it reads ten times, clear the out of range reads, and returns the average. 
	 * @param sens the sensor to read from
	 * @return the distance, in cm.
	 */
	public static double readSonic(UltrasonicSensor sens) {
		double[] reads=new double[10];
		int cou=0;
		double ret, sum=0;
		for(int i=0;i<reads.length;i++)
			if((ret=sens.getDistance())<cutoffDist){
				cou++;
				sum+=ret;
			}
		if(cou!=0)
			return (sum/(double)cou);
		return cutoffDist;
	}
}
