package org.nkigen.eqr.agents.behaviours;

import org.nkigen.eqr.hospital.HospitalDetails;
import org.nkigen.eqr.messages.FireInitMessage;
import org.nkigen.eqr.messages.HospitalArrivalMessage;
import org.nkigen.eqr.messages.HospitalInitMessage;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

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
				try {
					Object content = msg.getContentObject();
					if (content instanceof HospitalInitMessage) {
						details = ((HospitalInitMessage) content).getHospital();
					} else if (content instanceof HospitalArrivalMessage) {
						System.out.println(myAgent.getLocalName()
								+ " Hospital has received patient: "
								+ ((HospitalArrivalMessage) content)
										.getPatient().getAID());
					}
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		} else {
			block();
		}

	}
}
