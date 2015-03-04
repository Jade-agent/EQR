package org.nkigen.eqr.emergencycontrol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jade.util.Logger;

import org.nkigen.eqr.agents.EQRAgentsHelper;
import org.nkigen.eqr.common.EmergencyResponseBase;
import org.nkigen.eqr.fires.FireDetails;
import org.nkigen.eqr.logs.EQRLogger;
import org.nkigen.eqr.messages.AmbulanceNotifyMessage;
import org.nkigen.eqr.messages.EQRRoutingError;
import org.nkigen.eqr.messages.EQRRoutingResult;
import org.nkigen.eqr.messages.FireEngineRequestMessage;
import org.nkigen.eqr.messages.MultipleRoutingRequestMessage;
import org.nkigen.eqr.messages.MultipleRoutingResponseMessage;
import org.nkigen.eqr.patients.PatientDetails;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class AssignFireEngineBehaviour extends SimpleBehaviour {

	public static final String FIREENGINE_ROUTER_ID = "fireengine_router_msg";
	boolean done = false;
	boolean req_r = false; /* Not yet made a request to the router */
	FireDetails fire;
	List<EmergencyResponseBase> bases;
	AID router;
 Logger logger;
	/* Assumption: This list of bases contain atleast an ambulance */
	public AssignFireEngineBehaviour(Agent a, FireDetails fd,
			List<EmergencyResponseBase> bases) {
		super(a);
		fire = fd;
		this.bases = bases;
		router = EQRAgentsHelper.locateRoutingServer(myAgent);
		logger = EQRLogger.prep(logger, myAgent.getLocalName());
		EQRLogger.log(logger, null, myAgent.getLocalName(), getBehaviourName()+ " init");
	}

	@Override
	public void action() {
		while (router == null)
			router = EQRAgentsHelper.locateRoutingServer(myAgent);
		MessageTemplate template = MessageTemplate.and(MessageTemplate.MatchSender(router),MessageTemplate.MatchConversationId(FIREENGINE_ROUTER_ID));
		
		ACLMessage msg = myAgent.receive(template);

		if (!req_r) {
			MultipleRoutingRequestMessage req = new MultipleRoutingRequestMessage();
			req.setBases((ArrayList<EmergencyResponseBase>) bases);
			req.setReply_to(myAgent.getAID());
			req.setTo(fire.getLocation());
			ACLMessage to_send = new ACLMessage(ACLMessage.REQUEST);
			to_send.addReceiver(router);
			to_send.setConversationId(FIREENGINE_ROUTER_ID);
			try {
				to_send.setContentObject(req);

				myAgent.send(to_send);
				req_r = true;
				EQRLogger.log(logger, to_send, myAgent.getLocalName(), getBehaviourName()+": Message sent to the Router:"+ router.getLocalName());
				System.out.println(getBehaviourName()
						+ " FIRE ENGINE ROUTE REQ SENt to ROUTER "
						+ fire.getAID());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (msg != null && req_r) {
			Object content = null;
			switch (msg.getPerformative()) {
			case ACLMessage.INFORM:

				try {
					content = msg.getContentObject();
					if (content instanceof MultipleRoutingResponseMessage) {
						System.out.println(getBehaviourName()
								+ " route found for fire_engine-fire");
						EQRLogger.log(logger, msg, myAgent.getLocalName(), getBehaviourName()+": Message received from router "+ router.getLocalName());
						informParties((MultipleRoutingResponseMessage) content);
						done = true;
						return;
					}
					else{
						EQRLogger
						.log(EQRLogger.LOG_EERROR,
								logger,
								msg,
								myAgent.getLocalName(),
								getBehaviourName()
										+ ": wrong message received ");
						
						myAgent.send(msg);
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

	private boolean isSameBase(EmergencyResponseBase b1,
			EmergencyResponseBase b2) {
		return b1.getLocation().getLatitude() == b2.getLocation().getLatitude()
				&& b1.getLocation().getLongitude() == b2.getLocation()
						.getLongitude();
	}

	private void informParties(MultipleRoutingResponseMessage msg) {
		/* TODO */
		EQRRoutingResult res = msg.getResult();

		ACLMessage to_fire = null;
		ACLMessage to_engine = null;
		if (res instanceof EQRRoutingError) {
			to_fire = new ACLMessage(ACLMessage.FAILURE);
			to_fire.addReceiver(fire.getAID());
			try {
				to_fire.setContentObject(res);
				EQRLogger.log(logger, to_fire, myAgent.getLocalName(), " Routing error encountered");
				myAgent.send(to_fire);
				done = true;
				return;
				/* TODO: Put some useful message here */
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		to_engine = new ACLMessage(ACLMessage.INFORM);
		to_fire = new ACLMessage(ACLMessage.INFORM);
		FireEngineRequestMessage fer = new FireEngineRequestMessage(
				FireEngineRequestMessage.NOTIFY_ENGINE);

		fer.setFire(fire);
		fer.setRoute(res);
		try {
			to_engine.setContentObject(fer);
			for (EmergencyResponseBase b : bases) {
				if (isSameBase(b, msg.getBase())) {
					System.out
							.println(getBehaviourName() + "BASES ARE EQUAL: ");
					to_engine.addReceiver(b.assignFireEngine());
					myAgent.send(to_engine);
					/* TODO: fix this */
				}
			}

			to_fire.setContentObject(new FireEngineRequestMessage(
					FireEngineRequestMessage.REPLY));
			to_fire.addReceiver(fire.getAID());
			EQRLogger.log(logger, to_engine, myAgent.getLocalName(), "Message sent to Fire Engine");
			EQRLogger.log(logger, to_fire, myAgent.getLocalName(), "Message sent to Fire");
			/* Add more useful info here for patient */
			myAgent.send(to_fire);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		done = true;
	}

	@Override
	public boolean done() {
		return done;
	}

}
