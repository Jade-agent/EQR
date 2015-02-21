package org.nkigen.eqr.common;

import jade.core.AID;

import java.io.Serializable;
import java.util.ArrayList;

import org.nkigen.maps.routing.EQRPoint;

public class EmergencyResponseBase implements Serializable {

	EQRPoint location;
	ArrayList<AID> responders;
	/*TODO: Implement status info here*/
	public EQRPoint getLocation() {
		return location;
	}
	public void setLocation(EQRPoint location) {
		this.location = location;
	}
	public ArrayList<AID> getResponders() {
		return responders;
	}
	public void setResponders(ArrayList<AID> responders) {
		this.responders = responders;
	}
}
