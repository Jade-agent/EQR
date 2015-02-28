package org.nkigen.eqr.messages;

import java.io.Serializable;

import org.nkigen.eqr.hospital.HospitalDetails;

public class HospitalInitMessage implements Serializable {

	HospitalDetails h;

	public HospitalDetails getHospital() {
		return h;
	}

	public void setHospital(HospitalDetails h) {
		this.h = h;
	}
}
