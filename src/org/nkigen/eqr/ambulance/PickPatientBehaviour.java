package org.nkigen.eqr.ambulance;

import java.util.ArrayList;
import java.util.List;

import org.nkigen.eqr.messages.AmbulanceNotifyMessage;
import org.nkigen.eqr.messages.EQRRoutingResult;
import org.nkigen.eqr.patients.PatientDetails;
import org.nkigen.maps.routing.EQRPoint;
import org.nkigen.maps.routing.graphhopper.EQRGraphHopperResult;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Plan:
 * - Just "drive" to the patient
 * - When you reach, notify the patient and "pick him/her"
 * @author nkigen
 *
 */
public class PickPatientBehaviour extends SimpleBehaviour {

	boolean done = false;
	PatientDetails patient;
	EQRGraphHopperResult route;
	AmbulanceDetails ambulance;
	public PickPatientBehaviour(Agent agent, AmbulanceNotifyMessage msg, AmbulanceDetails ambulance) {
		super(agent);
		this.patient = msg.getPatient();
		this.route = (EQRGraphHopperResult) msg.getResult();
		this.ambulance = ambulance;
	}
	/*Only receive message coming from the patient
	 *TODO: Also send out status messages 
	 */
	@Override
	public void action() {
		List<EQRPoint> points = route.getPoints();
		long duration = route.getDuration();
		double distance = route.getDistance();
	  for(EQRPoint p : points){
		  ambulance.setCurrentLocation(p);
		  
		  block((long)distance/duration);
	  }
	  ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
	  msg.addReceiver(patient.getAID());
	  /*TODO: send msg to patient and*/
	  done = true;
	}

	@Override
	public boolean done() {
		return done;
	}

}
