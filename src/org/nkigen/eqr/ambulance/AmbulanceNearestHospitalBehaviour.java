package org.nkigen.eqr.ambulance;

import org.nkigen.eqr.agents.EQRAgentsHelper;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;

public class AmbulanceNearestHospitalBehaviour extends SimpleBehaviour{

	boolean done = false;
	AmbulanceDetails ambulance;
	AID router;
	public AmbulanceNearestHospitalBehaviour(Agent agent, AmbulanceDetails ambulance) {
		super(agent);
		this.ambulance = ambulance;
		router = EQRAgentsHelper.locateRoutingServer(agent);
	}
	@Override
	public void action() {
		/*TODO: Implement this*/
		
	}

	@Override
	public boolean done() {
		return done;
	}

}
