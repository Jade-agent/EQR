package org.nkigen.eqr.fires;

import org.nkigen.eqr.common.EmergencyDetails;
import org.nkigen.eqr.common.EmergencyStateChangeInitiator;

public class FireDetails  extends EmergencyDetails{

	int status;
	EmergencyStateChangeInitiator listener;
	public FireDetails() {
	
	}
	public FireDetails(EmergencyStateChangeInitiator listener) {
		this.listener = listener;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
		if(listener!=null)
			listener.notifyStateChanged(this);
	}
	public EmergencyStateChangeInitiator getListener() {
		return listener;
	}

	public void setListener(EmergencyStateChangeInitiator listener) {
		this.listener = listener;
	}
}
