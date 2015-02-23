package org.nkigen.eqr.messages;

import java.io.Serializable;

import org.nkigen.eqr.fires.FireDetails;

public class FireInitMessage implements Serializable {

	FireDetails fire;

	public FireDetails getFire() {
		return fire;
	}

	public void setFire(FireDetails fire) {
		this.fire = fire;
	}
}
