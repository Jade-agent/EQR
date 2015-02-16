package org.nkigen.maps.viewer.updates;

import java.awt.Component;

public abstract class EQRStatusPanelItem {
	
	public static final int PATIENT_STATUS_ITEM = 0;
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

}
