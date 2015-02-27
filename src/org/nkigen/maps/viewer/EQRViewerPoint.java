package org.nkigen.maps.viewer;

import jade.core.AID;

import java.awt.Color;

import org.nkigen.eqr.common.EmergencyStatus;
import org.nkigen.eqr.messages.EQRLocationUpdate;
import org.nkigen.maps.routing.EQRPoint;

import java.io.Serializable;

public class EQRViewerPoint implements Serializable {

	/*
	 * Different Colors to be used
	 */
	public static final Color WAITING_PATIENT_COLOR = Color.YELLOW;
	public static final Color PICKED_PATIENT_COLOR = Color.PINK;
	public static final Color DEAD_PATIENT_COLOR = Color.BLACK;
	public static final Color DELIVERED_PATIENT_COLOR = Color.GREEN;

	public static final Color WAITING_FIRE_COLOR = Color.RED;
	public static final Color ATTENDED_FIRE_COLOR = Color.WHITE;

	public static final Color MOVING_AMBULANCE_COLOR = Color.RED;
	public static final Color WAITING_AMBULANCE_COLOR = Color.WHITE;

	public static final Color MOVING_FIRE_COLOR = Color.MAGENTA;
	public static final Color LOCATION_FIRE_COLOR = Color.BLUE;

	public static final Color HOSPITAL_COLOR = Color.BLUE;

	EQRPoint point;
	Color color;
	int status;
	boolean is_moving; /* For ambulances and fire engines */
	boolean is_dead; /* for patients */
	AID item_id;
	int type; /* For EQRStatusPanelitem types(Excluding stat ones) */

	public EQRViewerPoint(AID item) {
		this.item_id = item;
	}

	public EQRPoint getPoint() {
		return point;
	}

	public void setItemId(AID item) {
		this.item_id = item;
	}

	public void setIsDead(boolean bool) {
		is_dead = bool;
	}

	public boolean getIsDead() {
		return is_dead;
	}

	public AID getItemId() {
		return item_id;
	}

	public void setIsMoving(boolean bool) {
		is_moving = bool;
	}

	public boolean getIsMoving() {
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

		switch (type) {
		case EQRLocationUpdate.AMBULANCE_LOCATION:
			if (is_moving)
				color = MOVING_AMBULANCE_COLOR;
			else
				color = WAITING_AMBULANCE_COLOR;
			break;
		case EQRLocationUpdate.FIRE_ENGINE_LOCATION:
			if (is_moving)
				color = MOVING_FIRE_COLOR;
			else
				color = WAITING_FIRE_COLOR;
			break;
		case EQRLocationUpdate.FIRE_LOCATION:
			color = LOCATION_FIRE_COLOR;
			break;
		case EQRLocationUpdate.PATIENT_LOCATION:
			switch (status) {
			case EmergencyStatus.PATIENT_PICKED:
				color = PICKED_PATIENT_COLOR;
				break;
			case EmergencyStatus.PATIENT_WAITING:
				color = WAITING_PATIENT_COLOR;
				break;
			case EmergencyStatus.PATIENT_DELIVERED:
				color = DELIVERED_PATIENT_COLOR;
				break;
			}
			break;
		case EQRLocationUpdate.HOSPITAL_LOCATION:
			color = HOSPITAL_COLOR;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
}
