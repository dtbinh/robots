package rabat.ex2;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.bluetooth.RemoteDevice;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.IRSeekerV2;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.util.Delay;

public class coh {

	private ArrayList<BTConnection> connections;

	public coh() {
		connections = new ArrayList<BTConnection>();
	}

	public boolean connect(String name) {
		RemoteDevice btrd = Bluetooth.getKnownDevice(name);

		if (btrd == null) {
			LCD.clear();
			LCD.drawString("No such device", 0, 0);
			Button.waitForAnyPress();
			return false;
		}

		BTConnection btc = Bluetooth.connect(btrd);

		if (btc == null) {
			LCD.clear();
			LCD.drawString("Connect fail", 0, 0);
			Button.waitForAnyPress();
			return false;
		}
		connections.add(btc);
		return true;
	}

	public boolean sendMode(int mode) {
		for (BTConnection btc : connections) {
			DataOutputStream dos = btc.openDataOutputStream();
			try {
				dos.writeInt(mode);
				dos.flush();
				dos.close();
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	public static void main(String[] args) throws Exception {
		String name = "receiver2";
		IRSeekerV2 irSeeker = new IRSeekerV2(SensorPort.S2, IRSeekerV2.Mode.DC);
		coh trans = new coh();
		while (!trans.connect(name)) {
			LCD.drawString("Connected to",	0, 0);
			LCD.drawString(name,	0, 1);
			
		}
		while (true) {
			LCD.clear(); 
			int dir = irSeeker.getDirection();
			if (dir != 0) {
				trans.sendMode(dir);
				LCD.drawString("Goal!", 0, 4);
				Delay.msDelay(3000);
			}
			
		}
	}
}
