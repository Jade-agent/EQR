package org.nkigen.eqr.ambulance;

import org.nkigen.eqr.common.EmergencyDetails;
import org.nkigen.eqr.common.EmergencyStateChangeInitiator;
import org.nkigen.maps.routing.EQRPoint;

public class AmbulanceDetails extends EmergencyDetails{

	EQRPoint current_location;

	public EQRPoint getCurrentLocation() {
		return current_location;
	}

	public void setCurrentLocation(EQRPoint current_location) {
		this.current_location = current_location;
		EmergencyStateChangeInitiator.getInstance().notifyStateChanged(this);
	}
}
