package org.nkigen.eqr.messages;

import java.io.Serializable;

import org.nkigen.eqr.common.EmergencyDetails;

public class MissionCompleteNotificaton implements Serializable{
	public static final int AMBULANCE_MISSION = 0;
	public static final int FIREENGINE_MISSION = 1;
	
	int type;
	EmergencyDetails details;
	
	public MissionCompleteNotificaton(int type, EmergencyDetails details) {
		this.type = type;
		this.details = details;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public EmergencyDetails getDetails() {
		return details;
	}

	public void setDetails(EmergencyDetails o) {
		details = o;
	}

}
