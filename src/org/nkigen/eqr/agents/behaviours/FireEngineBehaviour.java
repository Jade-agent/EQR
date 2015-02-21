package org.nkigen.eqr.agents.behaviours;

import org.nkigen.eqr.common.EmergencyDetails;
import org.nkigen.eqr.common.EmergencyStateChangeInitiator;
import org.nkigen.eqr.common.EmergencyStateChangeListener;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

public class FireEngineBehaviour extends CyclicBehaviour implements EmergencyStateChangeListener{

	public FireEngineBehaviour(Agent agent) {
		super(agent);
		EmergencyStateChangeInitiator.getInstance().addListener(this);
	}

	@Override
	public void action() {
	}

	@Override
	public void onEmergencyStateChange(EmergencyDetails ed) {
		// TODO Auto-generated method stub
		
	}

}
