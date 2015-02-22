package org.nkigen.eqr.hospital;

import java.util.ArrayList;

import org.nkigen.eqr.common.EmergencyDetails;
import org.nkigen.eqr.patients.PatientDetails;

public class HospitalDetails extends EmergencyDetails {

	ArrayList<PatientDetails> patients;

	public HospitalDetails() {

	}

	public void newPatient(PatientDetails patient) {
		this.patients.add(patient);
	}

	public int getNumPatients() {
		return patients.size();
	}

	public ArrayList<PatientDetails> getPatients() {
		return patients;
	}

}
