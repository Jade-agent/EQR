package org.nkigen.eqr.patients;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;

public class RequestAmbulanceBehaviour extends SimpleBehaviour {

	PatientDetails patient;
	boolean done = false;
	
	public RequestAmbulanceBehaviour(Agent a, PatientDetails p){
		super(a);
		patient = p;
	}
	
	@Override
	public void action() {
	/*
	 * Request for ambulance
	 * Start timer and change patient status accordingly
	 * Transfer patient to ambulance	
	 */
	}

	@Override
	public boolean done() {
		return done;
	}

}
