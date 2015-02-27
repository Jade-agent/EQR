package org.nkigen.eqr.patients;

import java.io.IOException;

import org.nkigen.eqr.agents.EQRAgentsHelper;
import org.nkigen.eqr.messages.AmbulanceRequestMessage;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class RequestAmbulanceBehaviour extends SimpleBehaviour {

	PatientDetails patient;
	boolean done = false;
	boolean req_made = false;
	AID command_center = null;

	public RequestAmbulanceBehaviour(Agent a, PatientDetails p) {
		super(a);
		patient = p;
	}

	@Override
	public void action() {
		if (command_center == null)
			command_center = EQRAgentsHelper.locateControlCenter(myAgent);
		ACLMessage msg = myAgent.receive();
		if (msg == null && !req_made) {
			msg = new ACLMessage(ACLMessage.REQUEST);
			AmbulanceRequestMessage arm = new AmbulanceRequestMessage();
			arm.setPatient(patient);
			try {
				msg.setContentObject(arm);
				msg.addReceiver(command_center);
				myAgent.send(msg);
				req_made = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println(getBehaviourName() + " "
					+ myAgent.getLocalName() + " Message sent to CC");
		} else if (msg != null && req_made) {
			switch (msg.getPerformative()) {
			case ACLMessage.CONFIRM:
				try {
					Object content = msg.getContentObject();
					if (content instanceof AmbulanceRequestMessage) {
						System.out.println(getBehaviourName()
								+ " response received from CC");
						done = true;
						return;
					}
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;

			}
		} else {
			if (msg != null) {
				myAgent.send(msg);
				done = true;
			}
		}
		/*
		 * Request for ambulance Start timer and change patient status
		 * accordingly Transfer patient to ambulance
		 */
	}

	@Override
	public boolean done() {
		return done;
	}

}
