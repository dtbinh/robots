package rabat.alpha;

import lejos.nxt.I2CPort;
import lejos.nxt.SensorConstants;
import lejos.nxt.addon.RCXSensorMultiplexer;

/**
 * don't working
 * @author Ohad Cohen <ohadcn@cs.huji.ac.il>
 * @author Ron Cohen
 *
 */
@Deprecated
public class MuxPort implements I2CPort {

//	private static int[] channels={0x22,0x27,0x2c,0x31};
	
	int mode=SensorConstants.MODE_RAW, type=SensorConstants.MIN_TYPE;
	RCXSensorMultiplexer mux;
	int channel;
	
	public MuxPort(RCXSensorMultiplexer mux, int channel) {
		this.mux=mux;
		this.channel=channel;
	}
	
	private void setChannel() {
		switch (channel) {
		case 1:
			mux.setChannelOne();
			break;
		case 2:
			mux.setChannelTwo();
			break;
		case 3:
			mux.setChannelThree();
			break;
		case 4:
			mux.setChannelFour();
			break;
		default:
			throw new UnsupportedOperationException("unknown chanel "+channel);
		}
	}
	
	@Override
	public int getMode() {
		setChannel();
		return mux.getPort().getMode();
	}

	@Override
	public int getType() {
		setChannel();
		return mux.getPort().getType();
	}

	@Override
	public void setMode(int mode) {
		setChannel();
		mux.getPort().setMode(mode);
		
	}

	@Override
	public void setType(int newType) {
        setChannel();
        mux.getPort().setType(newType);
	}

	@Override
	public void setTypeAndMode(int type, int mode) {
        setChannel();
		mux.getPort().setTypeAndMode(type, mode);
	}

	@Override
	public void i2cEnable(int mode) {
		setChannel();
		mux.getPort().i2cEnable(mode);
		
	}

	@Override
	public void i2cDisable() {
		setChannel();
		mux.getPort().i2cDisable();
		
	}

	@Override
	public int i2cStatus() {
		return mux.getPort().i2cStatus();
	}

	@Override
	public synchronized int i2cTransaction(int deviceAddress, byte[] writeBuf,
			int writeOffset, int writeLen, byte[] readBuf, int readOffset,
			int readLen) {
		return mux.getPort().i2cTransaction(deviceAddress, writeBuf, writeOffset, writeLen, readBuf, readOffset, readLen);
	}

}
