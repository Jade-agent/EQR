package org.nkigen.eqr.models;

import java.util.ArrayList;
import java.util.Random;

import org.nkigen.eqr.messages.EQRRoutingCriteria;
import org.nkigen.maps.routing.EQRPoint;
public class EmergencyHandler {

	public static EmergencyHandler handler;
	ArrayList<EQRPoint> vehicles_locations;
	ArrayList<EQRPoint> emergency_locations;
	int vehicles_available = 0;
	Random rn = new Random();
	int emergencies_waiting = 0;
	
	protected EmergencyHandler(){
		init_locations();
	}
	
	public static EmergencyHandler getInstance(){
		if(handler == null)
			handler = new EmergencyHandler();
		return handler;
	}
	
	public ArrayList<EQRPoint> getVehicles_locations() {
		return vehicles_locations;
	}


	public ArrayList<EQRPoint> getEmergency_locations() {
		return emergency_locations;
	}
	
	private void init_locations(){
		vehicles_locations = new  ArrayList<EQRPoint>();
		emergency_locations = new  ArrayList<EQRPoint>();
		
		vehicles_locations.add(new EQRPoint(46.071944, 11.119444));
		
		emergency_locations.add(new EQRPoint(46.056332, 11.133385));
		emergency_locations.add(new EQRPoint(46.046269, 11.134965));
		emergency_locations.add(new EQRPoint(46.0443813625, 11.1295622198));
		emergency_locations.add(new EQRPoint(46.0846000, 11.11145900));
	}
	
	public int getRandomIndex(int max){
		int min = 0;
		return rn.nextInt(max - min + 1) + min;
	}
	
	public synchronized EQRRoutingCriteria getRoute(){
		if(vehicles_available == 0 || emergencies_waiting == 0 )
			return null;
		--vehicles_available;
		--emergencies_waiting;
		int vehicle = getRandomIndex(vehicles_locations.size() - 1);
		int emergency = getRandomIndex(emergency_locations.size() - 1);
		System.out.println("Chosen Route: "+ vehicle + "  "+ emergency);
		return new EQRRoutingCriteria(vehicles_locations.get(vehicle),emergency_locations.get(emergency));
	}
	
	public int getVehicles_available() {
		return vehicles_available;
	}

	public void newEmergencyVehicle() {
		this.vehicles_available++;
	}

	public int getEmergencies_waiting() {
		return emergencies_waiting;
	}

	public void newEmergency() {
		this.emergencies_waiting++;
	}

	
	
}
