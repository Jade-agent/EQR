package org.nkigen.eqr.messages;

import org.nkigen.maps.routing.EQRPoint;

public class EQRLocationUpdate extends EQRUpdateMessage{

	public static final int AMBULANCE_LOCATION = 0;
	public static final int FIRE_ENGINE_LOCATION = 1;
	public static final int PATIENT_LOCATION = 2;
	public static final int FIRE_LOCATION = 3;
	
	int type;
	int item_id;
	EQRPoint current;
	EQRPoint heading;
	
	public EQRLocationUpdate(int type, int id){
		super();
		this.type = type;
		item_id = id;
	}

	public void setItemId(int item){
		this.item_id = item;
	}
	
	public int getItemId(){
		return item_id;
	}
	
	public EQRPoint getCurrent() {
		return current;
	}

	public void setCurrent(EQRPoint current) {
		this.current = current;
	}

	public EQRPoint getHeading() {
		return heading;
	}

	public void setHeading(EQRPoint heading) {
		this.heading = heading;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
