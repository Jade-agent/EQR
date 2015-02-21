package org.nkigen.eqr.messages;

import java.io.Serializable;

import org.nkigen.eqr.patients.PatientDetails;

public class AmbulanceRequestMessage implements Serializable {
	
	PatientDetails patient;

	public PatientDetails getPatient() {
		return patient;
	}

	public void setPatient(PatientDetails patient) {
		this.patient = patient;
	}

}
