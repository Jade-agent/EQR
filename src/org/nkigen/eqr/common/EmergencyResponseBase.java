package org.nkigen.eqr.common;

import jade.core.AID;

import java.io.Serializable;
import java.util.ArrayList;

import org.nkigen.maps.routing.EQRPoint;

public class EmergencyResponseBase implements Serializable {

	public static final int AMBULANCE_BASE = 0;
	public static final int FIREENGINE_BASE = 1;
	public static final int HOSPITAL_BASE = 2;
	EQRPoint location;
	ArrayList<AID> busy;
	ArrayList<AID> available; /*For hospitals only one item here*/
    int type;
	public EmergencyResponseBase(int type) {
		busy = new ArrayList<AID>();
		available = new ArrayList<AID>();
		this.type = type;
	}

	public int getType(){
		return type;
	}
	public EmergencyResponseBase(int type, EQRPoint location) {
		busy = new ArrayList<AID>();
		available = new ArrayList<AID>();
		this.location = location;
		this.type = type;
	}

	public EmergencyResponseBase(int type, EQRPoint location, ArrayList<AID> available) {
		busy = new ArrayList<AID>();
		this.available = available;
		this.location = location;
		this.type = type;
	}

	/* TODO: Implement status info here */
	public EQRPoint getLocation() {
		return location;
	}

	public void setLocation(EQRPoint location) {
		this.location = location;
	}

	public ArrayList<AID> getAvailable() {
		return available;
	}

	public void initResponders(ArrayList<AID> responders) {
		this.available = responders;
	}

	public AID assignAmbulance() {
		AID aid = null;
		if (available.size() > 0) {
			aid = available.get(0);
			available.remove(0);
			busy.add(aid);
		}
		return aid;
	}
}
