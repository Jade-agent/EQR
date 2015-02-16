package org.nkigen.maps.viewer.updates;

import java.awt.Component;

import org.nkigen.maps.routing.EQRPoint;

public class EQRFiresUpdatesItem extends EQRStatusPanelItem{

	EQRPoint location;
	EQRPoint closest_engine;
	long est_time_to_reach;
	@Override
	public Component getDisplayItem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayText() {
		String text = "<b>id</b> "+ getItemId()+
				", <b>location</b> ("+ location.getLatitude()+","+location.getLongitude()+") "
				+ "<b>Closest vehicle</b> ("+ closest_engine.getLatitude()+
				","+closest_engine.getLongitude()+")<b> Est Time</b> :"+
				closest_engine.getMillis()+ "<br/>";
		return text;
	}

	public EQRPoint getLocation() {
		return location;
	}

	public void setFireLocation(EQRPoint location) {
		this.location = location;
	}

	public EQRPoint getClosest_engine() {
		return closest_engine;
	}

	public void setClosest_engine(EQRPoint closest_engine) {
		this.closest_engine = closest_engine;
	}

	public long getEst_time_to_reach() {
		return est_time_to_reach;
	}

	public void setEst_time_to_reach(long est_time_to_reach) {
		this.est_time_to_reach = est_time_to_reach;
	}

}
