package org.nkigen.maps.viewer.updates;

import java.awt.Component;

import org.nkigen.maps.routing.EQRPoint;

public class EQRAmbulanceLocations extends EQRStatusPanelItem {

	EQRPoint location;
	boolean is_at_base;
	EQRPoint heading;
	
	
	
	public EQRAmbulanceLocations() {
		super();
		is_at_base = true;
	}

	@Override
	public Component getDisplayItem() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDisplayText() {
		 String text = "<b>id</b> "+ getStatusId();
				 if(is_at_base){
					 text+=", <b>Location </b> AT BASE<br />";
				 }
				 else{
					 	text += ", <b>location</b> ("+ location.getLatitude()+", "+location.getLongitude()+") " +
				    	 ", <b>Heading</b> ("+ heading.getLatitude()+", "+heading.getLongitude()+") " + "<br/>";
				 }
		 return text;
	}

	public EQRPoint getLocation() {
		return location;
	}

	public void setLocation(EQRPoint location) {
		this.location = location;
	}

	public boolean isIs_at_base() {
		return is_at_base;
	}

	public void isAtBase(boolean is_at_base) {
		this.is_at_base = is_at_base;
	}

	public EQRPoint getHeading() {
		return heading;
	}

	public void setHeading(EQRPoint heading) {
		this.heading = heading;
	}

}
