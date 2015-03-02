package org.nkigen.eqr.fires;

import java.io.IOException;
import jade.util.Logger;

import org.nkigen.eqr.agents.EQRAgentsHelper;
import org.nkigen.eqr.logs.EQRLogger;
import org.nkigen.eqr.messages.FireEngineRequestMessage;
import org.nkigen.eqr.patients.PatientDetails;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class RequestFireBehaviour extends SimpleBehaviour {

	FireDetails fd;
	boolean done = false;
	boolean req_made = false;
	AID command_center = null;
	Logger logger;

	public RequestFireBehaviour(Agent a, FireDetails fd) {
		super(a);
		this.fd = fd;
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
			FireEngineRequestMessage req = new FireEngineRequestMessage(
					FireEngineRequestMessage.REQUEST);
			req.setFire(fd);
			msg.addReceiver(command_center);
			try {
				msg.setContentObject(req);
				myAgent.send(msg);
				req_made = true;
				System.out.println(getBehaviourName() + " "
						+ myAgent.getLocalName() + " ENGINE REQUEST SENT");
				EQRLogger.log(logger, msg, myAgent.getLocalName(),
						getBehaviourName() + ": Message sent");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (msg != null && req_made) {
			EQRLogger.log(logger, msg, myAgent.getLocalName(),
					getBehaviourName() + ": Message Received");
			switch (msg.getPerformative()) {
			case ACLMessage.CONFIRM:
				try {
					Object content = msg.getContentObject();
					if (content instanceof FireEngineRequestMessage) {
						if (((FireEngineRequestMessage) content).getType() == FireEngineRequestMessage.REPLY) {
							System.out.println(getBehaviourName() + " "
									+ myAgent.getLocalName()
									+ " Fire Engine Coming!!");
							done = true;
						}
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
	}

	@Override
	public boolean done() {
		return done;
	}

}
