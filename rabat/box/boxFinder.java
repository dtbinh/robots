package rabat.box;

import java.io.DataOutputStream;
import java.io.IOException;

import javax.bluetooth.RemoteDevice;

import lejos.nxt.LCD;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

/**
 * most of the code is left on my colleges account, so have fun
 * @author Ohad Cohen <ohadcn@cs.huji.ac.il>
 *
 */
public class boxFinder {
	public static void main(String[] args) {
		LCD.drawString("BT wait", 0, 0);
		RemoteDevice btDev = Bluetooth.getKnownDevice("ohad-phone");
		NXTConnection connection = Bluetooth.connect(btDev);
		LCD.drawString("right BT",0, 0);
		DataOutputStream dataOut = connection.openDataOutputStream();
	    try {
	      dataOut.writeInt(1234);
	    } catch (IOException e ) {
	      System.out.println(" write error "+e); 
	    }
	}

}
