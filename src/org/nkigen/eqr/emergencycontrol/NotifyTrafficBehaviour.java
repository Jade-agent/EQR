package org.nkigen.eqr.emergencycontrol;

import java.io.IOException;
import java.util.ArrayList;

import org.nkigen.eqr.logs.EQRLogger;
import org.nkigen.eqr.messages.TrafficUpdateMessage;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

public class NotifyTrafficBehaviour extends OneShotBehaviour {

	TrafficUpdateMessage traffic;
	ArrayList<AID> subscribers;
	Logger logger;
	public NotifyTrafficBehaviour(Agent agent, TrafficUpdateMessage msg,
			ArrayList<AID> subscribers) {
		super(agent);
		traffic = msg;
		logger = EQRLogger.prep(logger, myAgent.getLocalName());
		this.subscribers = subscribers;
	}

	@Override
	public void action() {
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setConversationId(TrafficUpdateMessage.TRAFFIC_SUBSCRIBERS_CONV);
		for(AID recv : subscribers){
			msg.addReceiver(recv);
		}
		try {
			msg.setContentObject(traffic);
			myAgent.send(msg);
			EQRLogger.log(logger, msg, myAgent.getLocalName(), getBehaviourName()+": Traffic Update Message sent");
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
