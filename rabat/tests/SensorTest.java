package rabat.tests;

import rabat.Constants;
import rabat.Utils;
import lejos.nxt.Button;

public class SensorTest {

	public static void main(String[] args) {
		while (Button.waitForAnyPress()!=Button.ID_LEFT) {
			System.out.println(Utils.dbl2Str(Utils.readSonic(Constants.sensR)) +" "+Utils.dbl2Str(Utils.readSonic(Constants.sensF)) +" "
					+Utils.dbl2Str(Utils.readSonic(Constants.sensL)) +" "+Utils.dbl2Str(Constants.light.getLightValue()));
		}

	}

}
