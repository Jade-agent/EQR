package org.nkigen.eqr.patients;

import java.io.IOException;

import org.nkigen.eqr.agents.EQRAgentsHelper;
import org.nkigen.eqr.logs.EQRLogger;
import org.nkigen.eqr.messages.AmbulanceRequestMessage;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.util.Logger;

public class RequestAmbulanceBehaviour extends SimpleBehaviour {

	PatientDetails patient;
	boolean done = false;
	boolean req_made = false;
	AID command_center = null;
	Logger logger;

	public RequestAmbulanceBehaviour(Agent a, PatientDetails p) {
		super(a);
		patient = p;
		logger = EQRLogger.prep(logger, myAgent.getLocalName());
	}

	@Override
	public void action() {
		while (command_center == null)
			command_center = EQRAgentsHelper.locateControlCenter(myAgent);
		MessageTemplate temp = MessageTemplate.MatchSender(command_center);
		ACLMessage msg = myAgent.receive(temp);
		if (msg == null && !req_made) {
			msg = new ACLMessage(ACLMessage.REQUEST);
			AmbulanceRequestMessage arm = new AmbulanceRequestMessage();
			arm.setPatient(patient);
			try {
				msg.setContentObject(arm);
				msg.addReceiver(command_center);
				myAgent.send(msg);
				req_made = true;
				EQRLogger.log(logger, msg, myAgent.getLocalName(),
						getBehaviourName() + ": Message sent");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println(getBehaviourName() + " "
					+ myAgent.getLocalName() + " Message sent to CC");
		} else if (msg != null && req_made) {
			EQRLogger.log(logger, msg, myAgent.getLocalName(),
					getBehaviourName() + ": Message recevied");
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
				EQRLogger
						.log(EQRLogger.LOG_EERROR,
								logger,
								msg,
								myAgent.getLocalName(),
								getBehaviourName()
										+ ": Should not happen, wrong message received ");
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
