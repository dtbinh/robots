package rabat.tests;

import lejos.nxt.Button;
import lejos.nxt.Sound;
/**
 * don't working class
 * @author Ohad Cohen <ohadcn@cs.huji.ac.il>
 * @author Ron Cohen
 *
 */
public class TestSound {

	static int[] r={2,7,4,9,3};
	public static void main(String[] args) {
		while(Button.waitForAnyPress(3)!=0)
			Sound.twoBeeps();;

	}

}
