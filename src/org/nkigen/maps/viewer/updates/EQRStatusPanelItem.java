package org.nkigen.maps.viewer.updates;

import jade.core.AID;

import java.awt.Component;
import java.io.Serializable;

public abstract class EQRStatusPanelItem implements Serializable {

	public static final int PATIENT_STATUS_ITEM = 0;
	public static final int FIRE_STATUS_ITEM = 1;
	public static final int STAT_PATIENT_ITEM = 2;
	public static final int STAT_FIRE_ITEM = 3;
	public static final int AMBULANCE_LOCATION_ITEM = 4;
	public static final int FIRE_ENGINE_LOCATION_ITEM = 5;
	public static final int HOSPITAL_LOCATION_ITEM = 6;
	public static final int STATIC_PATIENT = 7;
	public static final int STATIC_FIRE = 8;

	static long num_stats;
	long status_id;
	AID item_id; // Id of ambulance, fire engine, patient, fire etc

	public EQRStatusPanelItem() {
		synchronized (this) {
			status_id = num_stats;
			num_stats++;
		}
	}

	public long getStatusId() {
		return status_id;
	}

	public abstract Component getDisplayItem();

	public abstract String getDisplayText();

	public AID getItem_id() {
		return item_id;
	}

	public void setItem_id(AID item_id) {
		this.item_id = item_id;
	}

}
