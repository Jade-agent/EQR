package org.nkigen.eqr.messages;

import jade.core.AID;

import java.io.Serializable;

import org.nkigen.eqr.common.EmergencyResponseBase;
import org.nkigen.eqr.hospital.HospitalDetails;
import org.nkigen.eqr.patients.PatientDetails;

public class AmbulanceNotifyMessage implements Serializable {

	public static final int NOTIFY_PATIENT_ROUTE = 0;
	public static final int NOTIFY_HOSPITAL_ROUTE = 1;

	EQRRoutingResult result;
	PatientDetails patient;
	EmergencyResponseBase hospital = null;
	int what;

	public AmbulanceNotifyMessage(int what) {
		this.what = what;
	}

	public int getNotificationType() {
		return what;
	}

	public EQRRoutingResult getResult() {
		return result;
	}

	public void setResult(EQRRoutingResult result) {
		this.result = result;
	}

	public PatientDetails getPatient() {
		return patient;
	}

	public void setHospital(EmergencyResponseBase h) {
		this.hospital = h;
	}

	public EmergencyResponseBase getHospital() {
		return hospital;
	}

	public void setPatient(PatientDetails patient) {
		this.patient = patient;
	}
}
