package org.nkigen.eqr.messages;

import java.io.Serializable;

public class TrafficUpdateMessage implements Serializable {
	public static final String TRAFFIC_SUBSCRIBERS_CONV = "traffic_subscribers";
	public static final int TRAFFIC_HIGH = 0;
	public static final int TRAFFIC_MEDIUM = 1;
	public static final int TRAFFIC_LOW = 2;

	int traffic;
	long delay;
	double rate = 0.01;
	
	public double getSimulationRate(){
		return rate;
	}
	boolean is_subscribed = false;
	
	public boolean isSubscribed(){
		return is_subscribed;
	}
	public void subscribe(){
		is_subscribed = true;
	}
	public void unsubscribe(){
		is_subscribed = false;
	}
	public TrafficUpdateMessage(int traffic) {
		this.traffic = traffic;
	}
	public TrafficUpdateMessage() {
	}

	public int getTraffic() {
		return traffic;
	}

	public void setTraffic(int traffic) {
		this.traffic = traffic;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

}
