package org.nkigen.eqr.messages;

import java.io.Serializable;

import org.nkigen.eqr.ambulance.AmbulanceDetails;
import org.nkigen.eqr.patients.PatientDetails;

public class PickPatientMessage implements Serializable {

	AmbulanceDetails ambulance;
	PatientDetails patient;
	public PickPatientMessage() {
		// TODO Auto-generated constructor stub
	}
	

	public AmbulanceDetails getAmbulance() {
		return ambulance;
	}

	public void setAmbulance(AmbulanceDetails ambulance) {
		this.ambulance = ambulance;
	}


	public PatientDetails getPatient() {
		return patient;
	}


	public void setPatient(PatientDetails patient) {
		this.patient = patient;
	}
}
