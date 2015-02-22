package org.nkigen.eqr.agents.behaviours;

import org.nkigen.eqr.hospital.HospitalDetails;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

public class HospitalBehaviour extends CyclicBehaviour {

	HospitalDetails details;

	public HospitalBehaviour(Agent agent) {
		super(agent);
	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();

		if (msg != null) {
			switch (msg.getPerformative()) {
			case ACLMessage.INFORM:
				break;
			}
		} else {
			block();
		}

	}

}
