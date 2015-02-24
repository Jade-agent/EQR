package org.nkigen.eqr.ambulance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.nkigen.eqr.messages.AmbulanceNotifyMessage;
import org.nkigen.eqr.messages.EQRRoutingResult;
import org.nkigen.eqr.messages.PickPatientMessage;
import org.nkigen.eqr.patients.PatientDetails;
import org.nkigen.maps.routing.EQRPoint;
import org.nkigen.maps.routing.graphhopper.EQRGraphHopperResult;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * Plan: - Just "drive" to the patient - When you reach, notify the patient and
 * "pick him/her"
 * 
 * @author nkigen
 *
 */
public class PickPatientBehaviour extends SimpleBehaviour {

	boolean done = false;
	PatientDetails patient;
	EQRGraphHopperResult route;
	AmbulanceDetails ambulance;

	public PickPatientBehaviour(Agent agent, AmbulanceNotifyMessage msg,
			AmbulanceDetails ambulance) {
		super(agent);
		this.patient = msg.getPatient();
		this.route = (EQRGraphHopperResult) msg.getResult();
		this.ambulance = ambulance;
	}

	/*
	 * Only receive message coming from the patientTODO: Also send out status
	 * messages
	 */
	@Override
	public void action() {
		List<EQRPoint> points = route.getPoints();

		long duration = route.getDuration();
		double distance = route.getDistance();
		System.out.println(getBehaviourName() + " " + myAgent.getLocalName()
				+ " duration " + duration + " dist " + distance + " points "
				+ route.getPoints().size() + " sleep " + (long) duration
				/ route.getPoints().size());
		for (EQRPoint p : points) {

			ambulance.setCurrentLocation(p);
			// System.out.println(myAgent.getLocalName()+ " loc: "+ p);

		//	try {
			//	Thread.sleep((long) duration / route.getPoints().size());
			
		//	} catch (InterruptedException e) {
				// TODO Auto-generated catch block
			//	e.printStackTrace();
		//	}
		}
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
		msg2.addReceiver(myAgent.getAID());
		msg.addReceiver(patient.getAID());
		PickPatientMessage ppm = new PickPatientMessage();
		PickPatientMessage ppm2 = new PickPatientMessage();
		ppm.setAmbulance(ambulance);
		ppm2.setPatient(patient);
		try {
			msg.setContentObject(ppm);
			msg2.setContentObject(ppm2);
			myAgent.send(msg);
			myAgent.send(msg2);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* TODO: send msg to patient and */
		done = true;
	}

	@Override
	public boolean done() {
		return done;
	}

}
