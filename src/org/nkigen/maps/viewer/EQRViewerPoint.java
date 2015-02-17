package org.nkigen.maps.viewer;

import java.awt.Color;

import org.nkigen.maps.routing.EQRPoint;

public class EQRViewerPoint {

	public static final Color WAITING_PATIENT_COLOR = Color.RED;
	public static final Color DEAD_PATIENT_COLOR = Color.RED;
	public static final Color WAITING_FIRE_COLOR = Color.RED;
	public static final Color ATTENDED_FIRE_COLOR = Color.RED;
	public static final Color TEAM_COLOR = Color.GREEN;
	public static final Color TRAVEL_COLOR = Color.BLUE;
	EQRPoint point;
	Color color;
	
	public EQRPoint getPoint() {
		return point;
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

	public EQRViewerPoint(EQRPoint point, Color color) {
	  this.point = point;
	  this.color = color;
	}
}
