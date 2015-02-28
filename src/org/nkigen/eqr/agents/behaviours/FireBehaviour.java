package org.nkigen.eqr.agents.behaviours;

import java.io.IOException;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import org.nkigen.eqr.agents.EQRAgentsHelper;
import org.nkigen.eqr.common.EmergencyDetails;
import org.nkigen.eqr.common.EmergencyStateChangeInitiator;
import org.nkigen.eqr.common.EmergencyStateChangeListener;
import org.nkigen.eqr.common.EmergencyStatus;
import org.nkigen.eqr.fires.FireDetails;
import org.nkigen.eqr.fires.FireGoals;
import org.nkigen.eqr.messages.AmbulanceInitMessage;
import org.nkigen.eqr.messages.EQRLocationUpdate;
import org.nkigen.eqr.messages.FireInitMessage;

public class FireBehaviour extends CyclicBehaviour implements
		EmergencyStateChangeListener {

	FireDetails fire;
	FireGoals goals;
	EmergencyStateChangeInitiator listener;

	public FireBehaviour(Agent agent) {
		super(agent);
		listener = new EmergencyStateChangeInitiator();
		listener.addListener(this);
		goals = new FireGoals();
	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		if (msg != null) {
			switch (msg.getPerformative()) {
			case ACLMessage.INFORM:
				try {
					Object content = msg.getContentObject();
					if (content instanceof FireInitMessage) {
						fire = ((FireInitMessage) content).getFire();
						fire.setListener(listener);
						System.out.println(getBehaviourName()
								+ " Fire init recieved "
								+ myAgent.getLocalName());
						fire.setStatus(EmergencyStatus.FIRE_STATUS_ACTIVE);
					}
				} catch (UnreadableException e) {
					e.printStackTrace();
				}
				break;
			}
		} else {
			block();
		}

	}

	private void sendFireUpdate(int status) {
		System.out.println(getBehaviourName() + ": " + myAgent.getLocalName()
				+ " Fire sending init location");
		AID update = EQRAgentsHelper.locateUpdateServer(myAgent);
		EQRLocationUpdate loc = new EQRLocationUpdate(
				EQRLocationUpdate.FIRE_LOCATION, myAgent.getAID());
		loc.setStatus(status);
		loc.setIsMoving(false);
		loc.setIsDead(false);
		loc.setCurrent(fire.getLocation());
		ACLMessage msg = new ACLMessage(ACLMessage.PROPAGATE);
		msg.addReceiver(update);
		try {
			msg.setContentObject(loc);
			myAgent.send(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onEmergencyStateChange(EmergencyDetails ed) {
		// TODO Auto-generated method stub
		Object[] params = new Object[2];
		Behaviour behaviour = null;
		if (ed instanceof FireDetails) {
			switch (((FireDetails) ed).getStatus()) {
			case EmergencyStatus.FIRE_STATUS_ACTIVE:
				sendFireUpdate(EmergencyStatus.FIRE_STATUS_ACTIVE);
				params[0] = myAgent;
				params[1] = fire;
				behaviour = goals.executePlan(FireGoals.REQUEST_FIRE_ENGINE,
						params);
				break;
			case EmergencyStatus.FIRE_STATUS_OFF:
				sendFireUpdate(EmergencyStatus.FIRE_STATUS_OFF);
				break;
			}
			if (behaviour != null) {
				myAgent.addBehaviour(behaviour);
			} else {
				System.out.println(getBehaviourName()
						+ " behaviour returned null " + myAgent.getLocalName());
			}
		}

	}

}
