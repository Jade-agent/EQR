package org.nkigen.eqr.agents.behaviours;

import java.util.ArrayList;
import java.util.List;

import org.nkigen.eqr.agents.EQRAgentsHelper;
import org.nkigen.eqr.messages.EQRRoutingResult;
import org.nkigen.eqr.models.EQREmergencyPoint;
import org.nkigen.maps.routing.EQRPoint;
import org.nkigen.maps.routing.graphhopper.EQRGraphHopperResult;
import org.nkigen.maps.viewer.updates.EQRAmbulanceLocations;
import org.nkigen.maps.viewer.updates.EQRPatientStatusItem;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;

public class AmbulanceManagerBehaviour extends SimpleBehaviour {

	boolean done;
	EQRRoutingResult route;
	EQREmergencyPoint ambulance;
	EQREmergencyPoint patient;
	AID update_server;

	List<EQRPoint> route_to_patient;
	long duration;
	int next_index;
	public AmbulanceManagerBehaviour(Agent agent,EQRRoutingResult route,
			EQREmergencyPoint ambulance, EQREmergencyPoint patient) {
		super(agent);
		this.ambulance = ambulance;
		this.patient = patient;
		this.route = route;
		if(route instanceof EQRGraphHopperResult){
			route_to_patient = ((EQRGraphHopperResult) route).getPoints();
			duration = ((EQRGraphHopperResult) route).getDuration();
		}
		update_server = EQRAgentsHelper.locateUpdateServer(myAgent);
	}

	@Override
	public void action() {
		
		/*TODO: Add traffic data*/
		if(next_index < route_to_patient.size()){
			EQRPoint point = route_to_patient.get(next_index);
			EQRPatientStatusItem patient_loc = new EQRPatientStatusItem();
			EQRAmbulanceLocations loc = new EQRAmbulanceLocations();
			patient_loc.setClosest_vehicle_loc(new EQRPoint(ambulance.getLatitude(), patient.getLongitude()));
		}

	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
