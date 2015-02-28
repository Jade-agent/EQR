package org.nkigen.eqr.emergencycontrol;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.nkigen.eqr.agents.EQRAgentsHelper;
import org.nkigen.eqr.common.EmergencyResponseBase;
import org.nkigen.eqr.messages.BaseRouteMessage;
import org.nkigen.eqr.messages.EQRRoutingError;
import org.nkigen.eqr.messages.EQRRoutingResult;
import org.nkigen.eqr.messages.MultipleRoutingRequestMessage;
import org.nkigen.maps.routing.EQRPoint;
import org.nkigen.maps.routing.graphhopper.EQRGraphHopperResult;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class ResponderToBaseBehaviour extends SimpleBehaviour {

	ACLMessage reply;
	BaseRouteMessage brm;
	boolean done = false;
	boolean req_r = false;
	AID router;

	public ResponderToBaseBehaviour(Agent agent, ACLMessage reply,
			BaseRouteMessage brm) {
		super(agent);
		this.reply = reply;
		this.brm = brm;
	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		if (msg == null && !req_r) {

			if (router == null)
				router = EQRAgentsHelper.locateRoutingServer(myAgent);
			ACLMessage to_send = new ACLMessage(ACLMessage.REQUEST);
			to_send.addReceiver(router);
			try {
				System.out.println(myAgent.getLocalName()
						+ " Sending Router a request to be backtobase "
						+ getBehaviourName());
				to_send.setContentObject(brm.getCriteria());
				myAgent.send(to_send);
				req_r = true;

			} catch (IOException e) {
				// TODO Auto-generated catch block

				e.printStackTrace();
			}

		} else if (msg != null && req_r) {
			switch (msg.getPerformative()) {
			case ACLMessage.INFORM:
				Object content;
				try {
					content = msg.getContentObject();
					if (content instanceof EQRRoutingResult) {
						try {
							BaseRouteMessage base = new BaseRouteMessage(BaseRouteMessage.REPLY);
							base.setResult((EQRRoutingResult) content);
							reply.setContentObject(base);
							reply.setPerformative(ACLMessage.INFORM);
							System.out.println(myAgent.getLocalName()+" message sent to "+ (AID)reply.getAllReceiver().next());
							myAgent.send(reply);
							done = true;
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					done = true;
					return;
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
				return;
			}
		}

	}

	@Override
	public boolean done() {
		return done;
	}

}
