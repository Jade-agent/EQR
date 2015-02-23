package org.nkigen.eqr.messages;

import java.io.Serializable;
import java.util.List;

import org.nkigen.eqr.common.EmergencyResponseBase;

public class ControlCenterInitMessage implements Serializable{
	List<EmergencyResponseBase> ambulance_bases;
	List<EmergencyResponseBase> hospital_bases;
	List<EmergencyResponseBase> fire_engine_bases;
	public List<EmergencyResponseBase> getAmbulance_bases() {
		return ambulance_bases;
	}
	public void setAmbulance_bases(List<EmergencyResponseBase> ambulance_bases) {
		this.ambulance_bases = ambulance_bases;
	}
	public List<EmergencyResponseBase> getHospital_bases() {
		return hospital_bases;
	}
	public void setHospital_bases(List<EmergencyResponseBase> hospital_bases) {
		this.hospital_bases = hospital_bases;
	}
	public List<EmergencyResponseBase> getFire_engine_bases() {
		return fire_engine_bases;
	}
	public void setFire_engine_bases(List<EmergencyResponseBase> fire_engine_bases) {
		this.fire_engine_bases = fire_engine_bases;
	}
}
