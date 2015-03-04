package rabat.ex2;
import java.io.DataInputStream;
import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.NXTConnection;

public class BTReceiver implements Runnable {

	Thread listenThread;
	private static final String[] modesStrings = { "Start" ,"Stop", "Normal", "Fast", "Exit" };
	private boolean shouldRun = false;
	private int mode;
	private boolean isConnected;
	
	
	public BTReceiver() {
		mode = 0;
		isConnected = false;
	}

	/**
	 * @return the mode
	 */
	public int getMode() {
		synchronized (this) {
			return mode;
		}
	}
	
	@Override
	public void run() {

		String connected = "Connected";
		String waiting = "Waiting...";
		LCD.drawString(waiting, 0, 0);
		NXTConnection connection = Bluetooth.waitForConnection();
		isConnected = true;
		LCD.clear();
		LCD.drawString(connected, 0, 0);
		DataInputStream dis = connection.openDataInputStream();
		
		while (shouldRun) {
			try {
				mode = dis.readInt();
				dis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void stop() {
		shouldRun = false;
		try {
			listenThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void start() {
		shouldRun = true;
		listenThread = new Thread(this);
		listenThread.start();
	}

	public boolean isConnected() {
		synchronized (this) {
			return isConnected;
		}
	}

	public void resetMode() {
		synchronized (this) {
			mode = 0;
		}
	}

//	public static void main(String[] args) throws Exception {
//		BTReceiver rec = new BTReceiver();
//		rec.start();
//		int mode = rec.getMode();
//		while(true) {
//			int newMode = rec.getMode();
//			if (mode != newMode) {
//				mode = newMode;
//				LCD.clear(1);
//				LCD.clear(2);
//				LCD.drawString("Mode Changed!",	0, 1);
//				LCD.drawString(modesStrings[mode], 0, 2);
//			}
//			if (mode == BTTransmitter.MODE_EXIT) {
//				LCD.clear();
//				LCD.drawString("Exiting..",	0, 0);
//				rec.stop();
//				LCD.drawString("Exited",	0, 1);
//				break;
//			}
//		}
//	}


}