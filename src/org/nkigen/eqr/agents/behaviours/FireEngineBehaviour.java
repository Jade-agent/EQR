package org.nkigen.eqr.agents.behaviours;

import java.io.IOException;

import org.nkigen.eqr.agents.EQRAgentsHelper;
import org.nkigen.eqr.ambulance.AmbulanceDetails;
import org.nkigen.eqr.common.EmergencyDetails;
import org.nkigen.eqr.common.EmergencyStateChangeInitiator;
import org.nkigen.eqr.common.EmergencyStateChangeListener;
import org.nkigen.eqr.fireengine.FireEngineDetails;
import org.nkigen.eqr.fireengine.FireEngineGoals;
import org.nkigen.eqr.messages.AmbulanceInitMessage;
import org.nkigen.eqr.messages.AttendToFireMessage;
import org.nkigen.eqr.messages.EQRLocationUpdate;
import org.nkigen.eqr.messages.FireEngineInitMessage;
import org.nkigen.eqr.messages.FireEngineRequestMessage;
import org.nkigen.eqr.messages.FireInitMessage;

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

	public FireEngineBehaviour(Agent agent) {
		super(agent);
		listener = new EmergencyStateChangeInitiator();
		listener.addListener(this);
		goals = new FireEngineGoals();
	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		if (msg != null) {
			switch (msg.getPerformative()) {
			case ACLMessage.INFORM:
				try {
					Object content = msg.getContentObject();
					if (content instanceof FireEngineInitMessage) {
						details = ((FireEngineInitMessage) content)
								.getFireEngine();
						details.setListener(listener);
					} else if (content instanceof FireEngineRequestMessage) {
						FireEngineRequestMessage req = (FireEngineRequestMessage) content;
						if (req.getType() == FireEngineRequestMessage.NOTIFY_ENGINE) {

						}
					} else if (content instanceof FireEngineRequestMessage) {
						/* Attend to fire */
						FireEngineRequestMessage req = (FireEngineRequestMessage) content;
						if (req.getType() == FireEngineRequestMessage.NOTIFY_ENGINE) {
							/* Handle this */
							Object[] params = new Object[3];
							params[0] = myAgent;
							params[1] = req;
							params[2] = details;
							Behaviour b = goals.executePlan(
									FireEngineGoals.ATTEND_TO_FIRE, params);
							if (b != null) {
								myAgent.addBehaviour(b);
							}
						}
					} else if (content instanceof AttendToFireMessage) {
						/* Now head back to base */
						AttendToFireMessage atf = (AttendToFireMessage) content;
						if (atf.getType() == AttendToFireMessage.TO_FIRE_ENGINE) {
							Object[] params = new Object[2];
							params[0] = myAgent;
							params[1] = details;
							Behaviour b = goals.executePlan(
									FireEngineGoals.BACK_TO_BASE, params);
							if (b != null) {
								myAgent.addBehaviour(b);
							}
						}
					} else {
						//myAgent.send(msg); // Requeue the message
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

	@Override
	public void onEmergencyStateChange(EmergencyDetails ed) {
		// TODO Auto-generated method stub
		if (ed instanceof FireEngineDetails) {
			EQRLocationUpdate loc = new EQRLocationUpdate(
					EQRLocationUpdate.FIRE_ENGINE_LOCATION,
					myAgent.getAID());
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
