package org.nkigen.eqr.messages;

import java.io.Serializable;

import org.nkigen.eqr.ambulance.AmbulanceDetails;
import org.nkigen.eqr.common.EmergencyResponseBase;

public class HospitalRequestMessage implements Serializable {

	public static final int HOSPITAL_REQUEST = 0;
	public static final int HOSPITAL_REPLY = 1;
	int which;
	Object[] msg;

	public HospitalRequestMessage(int which) {
		this.which = which;
	}

	public void setMessage(Object[] msg) {
		if (which == HOSPITAL_REQUEST) {
			this.msg = new Object[1];
			this.msg[0] = (AmbulanceDetails) msg[0];
		}
		/* We need the Hospital Details and the route */
		else if (which == HOSPITAL_REPLY) {
			this.msg = new Object[2];
			this.msg[0] = (EmergencyResponseBase) msg[0];
			this.msg[1] = (EQRRoutingResult) msg[1];
		}
	}

	public int getType() {
		return which;
	}

	public Object[] getMessage() {
		return msg;
	}
}
