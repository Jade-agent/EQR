package org.nkigen.eqr.agents.behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

import org.nkigen.eqr.common.EmergencyDetails;
import org.nkigen.eqr.common.EmergencyStateChangeInitiator;
import org.nkigen.eqr.common.EmergencyStateChangeListener;
import org.nkigen.eqr.models.EQREmergencyPoint;

public class AmbulanceBehaviour extends CyclicBehaviour implements EmergencyStateChangeListener{

	public AmbulanceBehaviour(Agent agent) {
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
