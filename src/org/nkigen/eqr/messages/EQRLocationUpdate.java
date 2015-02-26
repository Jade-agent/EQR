package org.nkigen.eqr.messages;

import jade.core.AID;

import org.nkigen.maps.routing.EQRPoint;

public class EQRLocationUpdate extends EQRUpdateMessage {

	public static final int AMBULANCE_LOCATION = 0;
	public static final int FIRE_ENGINE_LOCATION = 1;
	public static final int PATIENT_LOCATION = 2;
	public static final int FIRE_LOCATION = 3;

	int type;
	AID item_id;
	boolean is_moving;
	boolean is_dead = false;
	EQRPoint current;
	EQRPoint heading;

	public EQRLocationUpdate(int type, AID id) {
		super();
		this.type = type;
		item_id = id;
	}

	public void setIsDead(boolean bool) {
		is_dead = bool;
	}

	public boolean getIsDead() {
		return is_dead;
	}

	public void setIsMoving(boolean bool) {
		is_moving = bool;
	}

	public boolean getIsMoving() {
		return is_moving;
	}

	public void setItemId(AID item) {
		this.item_id = item;
	}

	public AID getItemId() {
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
