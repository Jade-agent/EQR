package org.nkigen.eqr.common;

import jade.core.AID;

import org.nkigen.maps.routing.EQRPoint;

public class EmergencyDetails {
	AID aid;
	int id;
	EQRPoint location;
	public AID getAID() {
		return aid;
	}
	public void setAID(AID aid) {
		this.aid = aid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public EQRPoint getLocation() {
		return location;
	}
	public void setLocation(EQRPoint location) {
		this.location = location;
	}
	
}
