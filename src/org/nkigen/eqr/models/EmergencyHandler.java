package org.nkigen.eqr.models;

import java.util.ArrayList;
import java.util.Random;

import org.nkigen.eqr.agents.EQRAgentsHelper;
import org.nkigen.eqr.messages.EQRRoutingCriteria;
import org.nkigen.maps.routing.EQRPoint;
public class EmergencyHandler {

	public static final int MAX_AMBULANCES_PER_BASE = 5;
	public static final int MAX_FIRE_ENGINES_PER_BASE = 5;
	public static EmergencyHandler handler;
	ArrayList<EQREmergencyPoint> vehicles_locations;
	ArrayList<EQREmergencyPoint> emergency_locations;
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
	
	public ArrayList<EQREmergencyPoint> getVehicles_locations() {
		return vehicles_locations;
	}


	public ArrayList<EQREmergencyPoint> getEmergency_locations() {
		return emergency_locations;
	}
	
	private void init_locations(){
		vehicles_locations = new  ArrayList<EQREmergencyPoint>();
		emergency_locations = new  ArrayList<EQREmergencyPoint>();
		
		EQREmergencyPoint b1 = new EQREmergencyPoint(46.071944, 11.119444, EQREmergencyPoint.AMBULANCE_BASE);
		EQREmergencyPoint b2 = new EQREmergencyPoint(46.071944, 11.119444, EQREmergencyPoint.FIRE_ENGINE_BASE);
		b1.setMax(MAX_AMBULANCES_PER_BASE);
		b2.setMax(MAX_FIRE_ENGINES_PER_BASE);
		b1.incrementCurrent();
		b2.incrementCurrent();
		vehicles_available++;
		vehicles_available++;
		vehicles_locations.add(b1);
		vehicles_locations.add(b2);
		
		EQREmergencyPoint c1 = new EQREmergencyPoint(46.056332, 11.133385, EQREmergencyPoint.PATIENT_EMERGENCY_POINT);
		EQREmergencyPoint c2 = new EQREmergencyPoint(46.046269, 11.134965, EQREmergencyPoint.PATIENT_EMERGENCY_POINT);
		EQREmergencyPoint c3 = new EQREmergencyPoint(46.0443813625, 11.1295622198, EQREmergencyPoint.FIRE_EMERGENCY_POINT);
		EQREmergencyPoint c4 = new EQREmergencyPoint(46.0846000, 11.11145900, EQREmergencyPoint.FIRE_EMERGENCY_POINT);
		
		emergencies_waiting++;
		emergencies_waiting++;
		emergencies_waiting++;
		emergencies_waiting++;
		emergency_locations.add(c1);
		emergency_locations.add(c2);
		emergency_locations.add(c3);
		emergency_locations.add(c4);
	}
	
	public int getRandomIndex(int max){
		int min = 0;
		return rn.nextInt(max - min + 1) + min;
	}
	
	public int getRandomIndex(int min, int max){
		return rn.nextInt(max - min + 1) + min;
	}
	
	public EQREmergencyPoint getPatientPoint(){
		if( emergencies_waiting == 0 )
			return null;
		for(int i=0; i < emergency_locations.size(); i++){
			if(emergency_locations.get(i).getType() == EQREmergencyPoint.PATIENT_EMERGENCY_POINT){
				EQREmergencyPoint p = emergency_locations.get(i);
				long deadline = getRandomIndex(EQREmergencyPoint.MIN_PATIENT_DEADLINE, EQREmergencyPoint.MAX_PATIENT_DEADLINE);
				p.setDeadline(deadline + EQRAgentsHelper.getCurrentTime());
				System.out.println("New Patient Point With Deadline: " + (deadline + EQRAgentsHelper.getCurrentTime()));
				emergency_locations.remove(i);
				emergencies_waiting--;
				return p;
			}
		}
		return null;
	}
	
	public EQREmergencyPoint getFirePoint(){
		if( emergencies_waiting == 0 )
			return null;
		for(int i=0; i < emergency_locations.size(); i++){
			if(emergency_locations.get(i).getType() == EQREmergencyPoint.FIRE_EMERGENCY_POINT){
				EQREmergencyPoint p = emergency_locations.get(i);
				emergency_locations.remove(i);
				emergencies_waiting--;
				return p;
			}
		}
		return null;
	}
	
	public EQREmergencyPoint getAmbulance(boolean remove){
		if( vehicles_available == 0 )
			return null;
		for(int i=0; i < vehicles_locations.size(); i++){
			if(vehicles_locations.get(i).getType() == EQREmergencyPoint.AMBULANCE_BASE){
				EQREmergencyPoint p = emergency_locations.get(i);
				if(p.getCurrent() > 0 && remove){
					p.decrementCurrent();
					vehicles_available--;
				}
				return p;
			}
		}
		return null;
	}
	public EQREmergencyPoint getFireEngine(boolean remove){
		if( vehicles_available == 0 )
			return null;
		for(int i=0; i < vehicles_locations.size(); i++){
			if(vehicles_locations.get(i).getType() == EQREmergencyPoint.FIRE_ENGINE_BASE){
				EQREmergencyPoint p = emergency_locations.get(i);
				if(p.getCurrent() > 0 && remove){
					p.decrementCurrent();
					vehicles_available--;
				}
				return p;
			}
		}
		return null;
	}
	
	public int getVehicles_available() {
		return vehicles_available;
	}

	public void newEmergencyVehicle() {
		int type = EQREmergencyPoint.AMBULANCE_BASE ;
		if(getRandomIndex(100) > 50){
			type = EQREmergencyPoint.FIRE_ENGINE_BASE;
		}
		for(int i=0; i < vehicles_locations.size(); i++){
			if(vehicles_locations.get(i).getType() == type){
				vehicles_locations.get(i).incrementCurrent();
				this.vehicles_available++;
				return;
				}
		}
	}

	public int getEmergencies_waiting() {
		return emergencies_waiting;
	}

	
	/*TODO: Generate Points From Map*/
	public void newEmergency() {
		int type = EQREmergencyPoint.PATIENT_EMERGENCY_POINT ;
		if(getRandomIndex(100) > 50){
			type = EQREmergencyPoint.FIRE_EMERGENCY_POINT;
		}
		for(int i=0; i < emergency_locations.size(); i++){
			if(emergency_locations.get(i).getType() == type){
				emergency_locations.get(i).incrementCurrent();
				this.emergencies_waiting++;
				return;
				}
		}
	}

	
	
}
