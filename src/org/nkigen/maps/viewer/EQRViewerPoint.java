package org.nkigen.maps.viewer;

import java.awt.Color;

import org.nkigen.eqr.messages.EQRLocationUpdate;
import org.nkigen.maps.routing.EQRPoint;
import org.nkigen.maps.viewer.updates.EQRAmbulanceLocations;
import org.nkigen.maps.viewer.updates.EQRFireEngineLocation;
import org.nkigen.maps.viewer.updates.EQRStatusPanelItem;
import java.io.Serializable;

public class EQRViewerPoint implements Serializable {

	/*
	 * Different Colors to be used
	 */
	public static final Color WAITING_PATIENT_COLOR = Color.RED;
	public static final Color DEAD_PATIENT_COLOR = Color.RED;
	public static final Color WAITING_FIRE_COLOR = Color.RED;
	public static final Color ATTENDED_FIRE_COLOR = Color.RED;
	public static final Color MOVING_AMBULANCE_COLOR = Color.GREEN;
	public static final Color WAITING_AMBULANCE_COLOR = Color.GREEN;
	public static final Color MOVING_FIRE_COLOR = Color.BLUE;
	public static final Color LOCATION_FIRE_COLOR = Color.BLUE;
	
	EQRPoint point;
	Color color;
	boolean is_moving; /*For ambulances and fire engines*/
	boolean is_dead; /*for patients */
	int item_id;
	int type; /*For EQRStatusPanelitem types(Excluding stat ones)*/
	public EQRViewerPoint(int item){
		this.item_id = item;
	}
	public EQRPoint getPoint() {
		return point;
	}

	public void setItemId(int item){
		this.item_id = item;
	}
	public void setIsDead(boolean bool){
		is_dead = bool;
	}

	public boolean getIsDead(){
		return is_dead;
	}
	public int getItemId(){
		return item_id;
	}
	public void setIsMoving(boolean bool){
		is_moving = bool;
	}

	public boolean getIsMoving(){
		return is_moving;
	}
	public void setPoint(EQRPoint point) {
		this.point = point;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	public void setColor() {
		switch(type){
		case EQRLocationUpdate.AMBULANCE_LOCATION:
			if(is_moving)
				color = MOVING_AMBULANCE_COLOR;
			else
				color = WAITING_AMBULANCE_COLOR;
			break;
		case EQRLocationUpdate.FIRE_ENGINE_LOCATION:
			if(is_moving)
				color = MOVING_FIRE_COLOR;
			else
				color = WAITING_FIRE_COLOR;
			break;
		case EQRLocationUpdate.FIRE_LOCATION:
				color =  LOCATION_FIRE_COLOR;
			break;
		case EQRLocationUpdate.PATIENT_LOCATION:
			if(is_dead)
				color = DEAD_PATIENT_COLOR;
			else
				color = WAITING_PATIENT_COLOR;
			break;
		}
	
	}

	public EQRViewerPoint(EQRPoint point, Color color) {
	  this.point = point;
	  this.color = color;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
}
