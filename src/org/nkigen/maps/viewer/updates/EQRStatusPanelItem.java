package org.nkigen.maps.viewer.updates;

import java.awt.Component;

public abstract class EQRStatusPanelItem {
	
	public static final int PATIENT_STATUS_ITEM = 0;
	public static final int FIRE_STATUS_ITEM = 1;
	public static final int STAT_PATIENT_ITEM = 2;
	public static final int STAT_FIRE_ITEM = 3;
	public static final int AMBULANCE_LOCATION_ITEM = 4;
	
	static long num_stats;
	long item_id;
	
	public EQRStatusPanelItem(){
		synchronized (this) {
			item_id = num_stats;
			num_stats++;
		}
	}
	public long getItemId(){
		return item_id;
	}
	public abstract Component getDisplayItem();
	public abstract String getDisplayText();

}
