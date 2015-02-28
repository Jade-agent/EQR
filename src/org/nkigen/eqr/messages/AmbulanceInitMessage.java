package org.nkigen.eqr.messages;

import java.io.Serializable;

import org.nkigen.eqr.ambulance.AmbulanceDetails;

public class AmbulanceInitMessage implements Serializable {

	AmbulanceDetails a;

	public AmbulanceDetails getAmbulance() {
		return a;
	}

	public void setAmbulance(AmbulanceDetails a) {
		this.a = a;
	}
}
