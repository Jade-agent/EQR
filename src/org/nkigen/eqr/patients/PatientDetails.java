package org.nkigen.eqr.patients;

import java.io.Serializable;

import jade.core.AID;

import org.nkigen.eqr.common.EmergencyDetails;
import org.nkigen.eqr.common.EmergencyStateChangeInitiator;
import org.nkigen.maps.routing.EQRPoint;

/**
 * Details related to a Patient
 * 
 * @author nkigen
 *
 */
public class PatientDetails extends EmergencyDetails {

	long deadline;
	int status;
	EmergencyStateChangeInitiator listener;

	public PatientDetails(EmergencyStateChangeInitiator listener) {
		// TODO Auto-generated constructor stub
		this.listener = listener;
	}

	public PatientDetails() {
		// TODO Auto-generated constructor stub
	}

	public long getDeadline() {
		return deadline;
	}

	public void setDeadline(long deadline) {
		this.deadline = deadline;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
		if (listener != null)
			listener.notifyStateChanged(this);
	}

	public EmergencyStateChangeInitiator getListener() {
		return listener;
	}

	public void setListener(EmergencyStateChangeInitiator listener) {
		this.listener = listener;
	}
}
