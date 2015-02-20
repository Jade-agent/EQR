package org.nkigen.eqr.emergencycontrol;

import org.nkigen.eqr.patients.PatientDetails;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;

public class AssignAmbulanceBehaviour extends SimpleBehaviour{

	boolean done = false;
	PatientDetails patient;
	public AssignAmbulanceBehaviour(Agent a, PatientDetails p) {
		super(a);
		patient = p;
	}
	@Override
	public void action() {
		
	}

	@Override
	public boolean done() {
		return done;
	}

}
