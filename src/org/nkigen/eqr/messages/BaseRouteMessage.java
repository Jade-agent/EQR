package org.nkigen.eqr.messages;

import java.io.Serializable;

public class BaseRouteMessage implements Serializable {
	public static final int REQUEST = 0;
	public static final int REPLY = 1;
	EQRRoutingCriteria criteria;
	EQRRoutingResult result;
	int type;

	public BaseRouteMessage(int type) {
		this.type = type;
	}

	public EQRRoutingCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(EQRRoutingCriteria criteria) {
		this.criteria = criteria;
	}

	public EQRRoutingResult getResult() {
		return result;
	}

	public void setResult(EQRRoutingResult result) {
		this.result = result;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}
