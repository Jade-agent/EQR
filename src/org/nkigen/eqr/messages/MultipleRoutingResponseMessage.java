package org.nkigen.eqr.messages;

import java.io.Serializable;

import org.nkigen.eqr.common.EmergencyResponseBase;

public class MultipleRoutingResponseMessage implements Serializable {
	EmergencyResponseBase base;
	EQRRoutingResult result;

	public EmergencyResponseBase getBase() {
		return base;
	}

	public void setBase(EmergencyResponseBase base) {
		this.base = base;
	}

	public EQRRoutingResult getResult() {
		return result;
	}

	public void setResult(EQRRoutingResult result) {
		this.result = result;
	}
}
