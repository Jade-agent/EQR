package org.nkigen.eqr.messages;

import java.io.Serializable;

public class ChangeEmergencyStatusMessage implements Serializable {

	public static final int TYPE_PATIENT = 0;
	public static final int TYPE_FIRE = 1;
	int type;
	int status;

	public ChangeEmergencyStatusMessage(int type, int status) {
		this.type = type;
		this.status = status;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
