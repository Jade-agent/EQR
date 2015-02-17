package org.nkigen.maps.viewer.updates;

import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;

import org.nkigen.maps.routing.EQRPoint;

public class EQRPatientStatusItem extends EQRStatusPanelItem{
	
	EQRPoint location;
	long deadline;
	long est_time_to_reach;
	EQRPoint closest_vehicle_loc;
	
	
	public EQRPatientStatusItem(){
		super();
	}
	public EQRPoint getPatientLocation() {
		return location;
	}
	public void setPatientLocation(EQRPoint location) {
		this.location = location;
	}
	public long getDeadline() {
		return deadline;
	}
	public void setDeadline(long deadline) {
		this.deadline = deadline;
	}
	public long getEst_time_to_reach() {
		return est_time_to_reach;
	}
	public void setEst_time_to_reach(long est_time_to_reach) {
		this.est_time_to_reach = est_time_to_reach;
	}
	public EQRPoint getClosest_vehicle_loc() {
		return closest_vehicle_loc;
	}
	public void setClosest_vehicle_loc(EQRPoint closest_vehicle_loc) {
		this.closest_vehicle_loc = closest_vehicle_loc;
	}
	@Override
	public Component getDisplayItem() {
		JLabel lbl = new JLabel();
		lbl.setFont(new Font("Serif", Font.PLAIN, 12));
		String text = "<html><b>id</b> "+ getStatusId()+
				", <b>location</b> ("+ location.getLatitude()+","+location.getLongitude()+") "
						+ "<b>Closest vehicle</b> ("+ closest_vehicle_loc.getLatitude()+
						","+closest_vehicle_loc.getLongitude()+")<b> Est Time</b> :"+
				closest_vehicle_loc.getMillis()+" <b> Deadline </b>"+deadline+"<br/></html>";
		lbl.setText(text);
		return lbl;
	}
	public String getDisplayText() {
		String text = "<b>id</b> "+ getStatusId()+
		", <b>location</b> ("+ location.getLatitude()+","+location.getLongitude()+") "
		+ "<b>Closest vehicle</b> ("+ closest_vehicle_loc.getLatitude()+
		","+closest_vehicle_loc.getLongitude()+")<b> Est Time</b> :"+
		closest_vehicle_loc.getMillis()+" <b> Deadline </b>"+deadline+"<br/>";
		return text;
	}

}
