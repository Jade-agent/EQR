package org.nkigen.eqr.messages;

import java.io.Serializable;

import org.nkigen.eqr.fireengine.FireEngineDetails;

public class FireEngineInitMessage implements Serializable {

	FireEngineDetails a;

	public FireEngineDetails getFireEngine() {
		return a;
	}

	public void setFireEngine(FireEngineDetails a) {
		this.a = a;
	}
}
