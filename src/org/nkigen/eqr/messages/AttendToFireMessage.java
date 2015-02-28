package org.nkigen.eqr.messages;

import java.io.Serializable;

import org.nkigen.eqr.fireengine.FireEngineDetails;
import org.nkigen.eqr.fires.FireDetails;

public class AttendToFireMessage implements Serializable{

	public static final int TO_FIRE = 0;
	public static final int TO_FIRE_ENGINE = 1;

	int type;
	FireDetails fire;
	FireEngineDetails engine;

	public AttendToFireMessage(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public FireDetails getFire() {
		return fire;
	}

	public void setFire(FireDetails fire) {
		this.fire = fire;
	}

	public FireEngineDetails getEngine() {
		return engine;
	}

	public void setEngine(FireEngineDetails engine) {
		this.engine = engine;
	}
}
