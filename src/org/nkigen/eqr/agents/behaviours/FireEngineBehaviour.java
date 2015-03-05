package org.nkigen.eqr.agents.behaviours;

import java.io.IOException;

import jade.util.Logger;

import org.nkigen.eqr.agents.EQRAgentsHelper;
import org.nkigen.eqr.ambulance.AmbulanceDetails;
import org.nkigen.eqr.common.EmergencyDetails;
import org.nkigen.eqr.common.EmergencyStateChangeInitiator;
import org.nkigen.eqr.common.EmergencyStateChangeListener;
import org.nkigen.eqr.fireengine.FireEngineDetails;
import org.nkigen.eqr.fireengine.FireEngineGoals;
import org.nkigen.eqr.logs.EQRLogger;
import org.nkigen.eqr.messages.AmbulanceInitMessage;
import org.nkigen.eqr.messages.AttendToFireMessage;
import org.nkigen.eqr.messages.EQRLocationUpdate;
import org.nkigen.eqr.messages.FireEngineInitMessage;
import org.nkigen.eqr.messages.FireEngineRequestMessage;
import org.nkigen.eqr.messages.FireInitMessage;
import org.nkigen.eqr.messages.TrafficUpdateMessage;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class FireEngineBehaviour extends CyclicBehaviour implements
		EmergencyStateChangeListener {

	FireEngineDetails details;
	EmergencyStateChangeInitiator listener;
	FireEngineGoals goals;
	Logger logger;
	TrafficUpdateMessage traffic;

	public FireEngineBehaviour(Agent agent) {
		super(agent);
		listener = new EmergencyStateChangeInitiator();
		listener.addListener(this);
		logger = EQRLogger.prep(logger, myAgent.getLocalName());
		goals = new FireEngineGoals();
	}

	@Override
	public void action() {
		/* TODO: Add msg templates */
		ACLMessage msg = myAgent.receive();
		if (msg != null) {
			EQRLogger.log(logger, msg, myAgent.getLocalName(),
					"Message received");
			switch (msg.getPerformative()) {
			case ACLMessage.INFORM:
				try {
					Object content = msg.getContentObject();
					if (content instanceof FireEngineInitMessage) {
						details = ((FireEngineInitMessage) content)
								.getFireEngine();
						details.setListener(listener);
						initLocation();
						TrafficUpdateMessage tum = new TrafficUpdateMessage();
						tum.subscribe();
						ACLMessage msg_tum = new ACLMessage(
								ACLMessage.SUBSCRIBE);
						AID ecc = EQRAgentsHelper.locateControlCenter(myAgent);
						while (ecc == null)
							ecc = EQRAgentsHelper.locateControlCenter(myAgent);
						msg_tum.addReceiver(ecc);
						try {
							msg_tum.setContentObject(tum);
							myAgent.send(msg_tum);
							EQRLogger.log(logger, msg_tum,
									myAgent.getLocalName(),
									" Traffic update subscription sent");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else if (content instanceof TrafficUpdateMessage) {
						this.traffic = (TrafficUpdateMessage) content;
					} else if (content instanceof FireEngineRequestMessage) {
						/* Attend to fire */
						FireEngineRequestMessage req = (FireEngineRequestMessage) content;
						if (req.getType() == FireEngineRequestMessage.NOTIFY_ENGINE) {
							/* Handle this */
							Object[] params = new Object[4];
							params[0] = myAgent;
							params[1] = req;
							params[2] = details;
							params[3] = traffic;
							Behaviour b = goals.executePlan(
									FireEngineGoals.ATTEND_TO_FIRE, params);
							if (b != null) {
								myAgent.addBehaviour(b);
							}else
								EQRLogger.log(logger, msg, myAgent.getLocalName(), "BEHAVIOUR NULL");
						}
						
					} else if (content instanceof AttendToFireMessage) {
						/* Now head back to base */
						AttendToFireMessage atf = (AttendToFireMessage) content;
						if (atf.getType() == AttendToFireMessage.TO_FIRE_ENGINE) {
							EQRLogger.log(logger, msg, myAgent.getLocalName(), "TO FIRE ENGINE");
							Object[] params = new Object[3];
							params[0] = myAgent;
							params[1] = details;
							params[2] = traffic;
							Behaviour b = goals.executePlan(
									FireEngineGoals.BACK_TO_BASE, params);
							if (b != null) {
								myAgent.addBehaviour(b);
							}
						}
						
					} else {
						// myAgent.send(msg); // Requeue the message
					}
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			block();
		}
	}

	private void initLocation() {
		EQRLogger.log(logger, null, myAgent.getLocalName(),
				" Initial location at " + details.getLocation());
		EQRLocationUpdate loc = new EQRLocationUpdate(
				EQRLocationUpdate.FIRE_ENGINE_LOCATION, myAgent.getAID());
		loc.setIsMoving(false);
		loc.setIsDead(false);
		loc.setCurrent(details.getLocation());
		loc.setHeading(details.getLocation());
		ACLMessage msg = new ACLMessage(ACLMessage.PROPAGATE);
		AID update = EQRAgentsHelper.locateUpdateServer(myAgent);
		msg.addReceiver(update);
		try {
			msg.setContentObject(loc);
			myAgent.send(msg);
			EQRLogger.log(logger, msg, myAgent.getLocalName(), "Message sent");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println(getBehaviourName() + " " + myAgent.getLocalName()
				+ " INIT LOCATION SENT ");

	}

	@Override
	public void onEmergencyStateChange(EmergencyDetails ed) {
		// TODO Auto-generated method stub
		EQRLogger.log(
				logger,
				null,
				myAgent.getLocalName(),
				"Location changed to "
						+ ((FireEngineDetails) ed).getCurrentLocation());
		if (ed instanceof FireEngineDetails) {
			EQRLocationUpdate loc = new EQRLocationUpdate(
					EQRLocationUpdate.FIRE_ENGINE_LOCATION, myAgent.getAID());
			loc.setIsMoving(true);
			loc.setIsDead(false);
			loc.setCurrent(((FireEngineDetails) ed).getCurrentLocation());
			loc.setHeading(ed.getLocation());
			ACLMessage msg = new ACLMessage(ACLMessage.PROPAGATE);
			AID update = EQRAgentsHelper.locateUpdateServer(myAgent);
			msg.addReceiver(update);
			try {
				msg.setContentObject(loc);
				myAgent.send(msg);
				// EQRLogger.log(logger, msg, myAgent.getLocalName(),
				// "Message sent");

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.out.println(getBehaviourName() + " "
					+ myAgent.getLocalName() + " Location changed to "
					+ ((FireEngineDetails) ed).getCurrentLocation());
		}

	}

}
