package rabat.tests;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.SensorPort;
import lejos.nxt.addon.RCXLightSensor;
import lejos.nxt.addon.RCXSensorMultiplexer;

public class MuxTest {

//	
//	public static void main(String[] args) {
//		Constants.compass.setAddress(0x10);
//		Constants.compass.getDegreesCartesian();
//		while(Button.waitForAnyPress()!=Button.ID_LEFT){
//			Constants.mux.setChannelOne();
//			System.out.println(Constants.light.getLightValue()+" "+Constants.compass.getDegreesCartesian());
//		}
//
//	}
	public static void main(String[] args) throws Exception {
		RCXSensorMultiplexer mux = new RCXSensorMultiplexer(SensorPort.S1);
		RCXLightSensor light = new RCXLightSensor(SensorPort.S1);
		
		while(true){
			mux.setChannelOne();
			System.out.print(light.getLightValue()+" ");
//			Thread.sleep(250);
			mux.setChannelTwo();
			System.out.print(light.getLightValue()+" ");
//			Thread.sleep(250);
			mux.setChannelThree();
			System.out.print(light.getLightValue()+" ");
//			Thread.sleep(250);
			mux.setChannelFour();
			System.out.print(light.getLightValue()+" ");
			Thread.sleep(250);

		}
	}

}
