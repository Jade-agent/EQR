package org.nkigen.eqr.fireengine;

import org.nkigen.eqr.common.EmergencyDetails;
import org.nkigen.eqr.common.EmergencyStateChangeInitiator;
import org.nkigen.maps.routing.EQRPoint;

public class FireEngineDetails extends EmergencyDetails {

	EmergencyStateChangeInitiator listener;
	boolean arrived = false;
	public FireEngineDetails() {
		// TODO Auto-generated constructor stub
	}

	EQRPoint current_location;

	public EQRPoint getCurrentLocation() {
		return current_location;
	}
	public void setArrived(boolean b){
		arrived = b;
	}
	public boolean getArrived(){
		return arrived;
	}
	public void setCurrentLocation(EQRPoint current_location) {
		this.current_location = current_location;
		if (listener != null)
			listener.notifyStateChanged(this);
	}

	public EmergencyStateChangeInitiator getListener() {
		return listener;
	}

	public void setListener(EmergencyStateChangeInitiator listener) {
		this.listener = listener;
	}
}
