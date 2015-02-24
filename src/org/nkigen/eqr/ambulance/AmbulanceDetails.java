package org.nkigen.eqr.ambulance;

import org.nkigen.eqr.common.EmergencyDetails;
import org.nkigen.eqr.common.EmergencyStateChangeInitiator;
import org.nkigen.maps.routing.EQRPoint;

public class AmbulanceDetails extends EmergencyDetails{

	EQRPoint current_location;
	EmergencyStateChangeInitiator listener;
	public AmbulanceDetails() {
		// TODO Auto-generated constructor stub
	}
	public AmbulanceDetails(EmergencyStateChangeInitiator listener) {
		// TODO Auto-generated constructor stub
		this.listener = listener;
	}
	
	public EQRPoint getCurrentLocation() {
		return current_location;
	}

	public void setCurrentLocation(EQRPoint current_location) {
		this.current_location = current_location;
		if(listener != null){
			listener.notifyStateChanged(this);	
		}
	}
	public EmergencyStateChangeInitiator getListener() {
		return listener;
	}
	public void setListener(EmergencyStateChangeInitiator listener) {
		this.listener = listener;
	}
}
