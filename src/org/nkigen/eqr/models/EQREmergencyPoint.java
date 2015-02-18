package org.nkigen.eqr.models;

import org.nkigen.maps.routing.EQRPoint;

public class EQREmergencyPoint extends EQRPoint {
	public static final int AMBULANCE_BASE = 0;
	public static final int FIRE_ENGINE_BASE = 1;
	public static final int FIRE_EMERGENCY_POINT = 2;
	public static final int PATIENT_EMERGENCY_POINT = 3;
	
	public static final int MIN_PATIENT_DEADLINE = 180 ; //180 seconds
	public static final int MAX_PATIENT_DEADLINE = 10*60 ; //600 seconds
	int max; // Maximum number of items in the Base Point
	int current;
	int type;
	long deadline;
	long id;
	public EQREmergencyPoint(double lat, double lon) {
		super(lat, lon);
	}
	
	public EQREmergencyPoint(double lat, double lon, int type) {
		super(lat, lon);
		this.type = type;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}
	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}
	public void incrementCurrent() {
		if(current < max)
			current++;
	}
	public void decrementCurrent() {
		if(current > 0)
			current--;
	}


	public boolean isFull() {
		return current == max;
	}

	public boolean isFree() {
		return current == 0;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getDeadline() {
		return deadline;
	}

	public void setDeadline(long deadline) {
		this.deadline = deadline;
	}
}
