package org.nkigen.eqr.messages;

import java.io.Serializable;

import org.nkigen.eqr.fires.FireDetails;

public class FireEngineRequestMessage implements Serializable{

	public static final int REQUEST = 0;
	public static final int REPLY = 1;
	public static final int NOTIFY_ENGINE = 2;
	FireDetails fire;
	EQRRoutingResult route;

	int type;
	public FireEngineRequestMessage(int type) {
		// TODO Auto-generated constructor stub
		this.type = type;
	}
	public int getType(){
		return type;
	}
	public FireDetails getFire() {
		return fire;
	}

	public void setFire(FireDetails fire) {
		this.fire = fire;
	}
	public EQRRoutingResult getRoute() {
		return route;
	}
	public void setRoute(EQRRoutingResult route) {
		this.route = route;
	}
}
