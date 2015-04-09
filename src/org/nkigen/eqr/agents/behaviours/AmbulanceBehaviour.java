package org.nkigen.eqr.agents.behaviours;

import java.io.IOException;

import jade.util.Logger;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import org.nkigen.eqr.agents.EQRAgentsHelper;
import org.nkigen.eqr.ambulance.AmbulanceDetails;
import org.nkigen.eqr.ambulance.AmbulanceGoals;
import org.nkigen.eqr.common.EmergencyDetails;
import org.nkigen.eqr.common.EmergencyStateChangeInitiator;
import org.nkigen.eqr.common.EmergencyStateChangeListener;
import org.nkigen.eqr.common.EmergencyStatus;
import org.nkigen.eqr.logs.EQRLogger;
import org.nkigen.eqr.messages.AmbulanceInitMessage;
import org.nkigen.eqr.messages.AmbulanceNotifyMessage;
import org.nkigen.eqr.messages.EQRLocationUpdate;
import org.nkigen.eqr.messages.HospitalArrivalMessage;
import org.nkigen.eqr.messages.PickPatientMessage;
import org.nkigen.eqr.messages.TrafficUpdateMessage;
import org.nkigen.maps.viewer.EQRViewerPoint;
import org.nkigen.maps.viewer.updates.EQRStatusPanelItem;

public class AmbulanceBehaviour extends CyclicBehaviour implements
		EmergencyStateChangeListener {

	AmbulanceGoals goals;
	AmbulanceDetails details;
	EmergencyStateChangeInitiator listener;
	Logger logger;
	TrafficUpdateMessage traffic;

	public AmbulanceBehaviour(Agent agent) {
		super(agent);
		listener = new EmergencyStateChangeInitiator();
		listener.addListener(this);
		logger = EQRLogger.prep(logger, myAgent.getLocalName());
		goals = new AmbulanceGoals();
	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		if (msg != null) {
			EQRLogger.log(logger, msg, myAgent.getLocalName(),
					"Message received");
			switch (msg.getPerformative()) {
			case ACLMessage.INFORM:
				try {
					Object content = msg.getContentObject();
					if (content instanceof AmbulanceNotifyMessage) {
						Object[] params = new Object[4];
						params[0] = myAgent;
						params[1] = (AmbulanceNotifyMessage) content;
						params[2] = details;
						params[3] = traffic;
						Behaviour b = goals.executePlan(
								AmbulanceGoals.PICK_PATIENT, params);
						if (b != null)
							myAgent.addBehaviour(b);
					} else if (content instanceof TrafficUpdateMessage) {
						this.traffic = (TrafficUpdateMessage) content;
					} else if (content instanceof AmbulanceInitMessage) {
						details = ((AmbulanceInitMessage) content)
								.getAmbulance();
						details.setListener(listener);
						sendAmbulanceInitLoc();
						TrafficUpdateMessage tum = new TrafficUpdateMessage();
						tum.subscribe();
						ACLMessage msg_tum = new ACLMessage(ACLMessage.SUBSCRIBE);
						AID ecc = EQRAgentsHelper.locateControlCenter(myAgent);
						while(ecc == null)
							ecc= EQRAgentsHelper.locateControlCenter(myAgent);
						msg_tum.addReceiver(ecc);
						try {
							msg_tum.setContentObject(tum);
							myAgent.send(msg_tum);
							EQRLogger.log(logger, msg_tum, myAgent.getLocalName(), " Traffic update subscription sent");
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
					} else if (content instanceof PickPatientMessage) {
						details.setArrived(false);
						System.out
								.println(myAgent.getLocalName()
										+ " : Trying to find the neareset hospital for "
										+ ((PickPatientMessage) content)
												.getPatient().getAID());
						Object[] params = new Object[4];
						params[0] = myAgent;
						params[1] = ((PickPatientMessage) content).getPatient();
						params[2] = details;
						params[3] = traffic;
						Behaviour b = goals.executePlan(
								AmbulanceGoals.TO_NEAREST_HOSPITAL, params);
						if (b != null)
							myAgent.addBehaviour(b);
					} else if (content instanceof HospitalArrivalMessage) {
						System.out
								.println(myAgent.getLocalName()
										+ " : Dropped Patient successfully. Now going back to base "
										+ ((HospitalArrivalMessage) content)
												.getPatient().getAID());
						Object[] params = new Object[3];
						params[0] = myAgent;
						params[1] = details;
						params[2] = traffic;
						Behaviour b = goals.executePlan(
								AmbulanceGoals.BACK_TO_BASE, params);
						if (b != null)
							myAgent.addBehaviour(b);

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

	private void sendAmbulanceInitLoc() {
		EQRLogger.log(logger, null, myAgent.getLocalName(),
				"Sending initial location");
		System.out.println(getBehaviourName() + ": " + myAgent.getLocalName()
				+ " Ambulance Sending");
		AID update = EQRAgentsHelper.locateUpdateServer(myAgent);
		EQRLocationUpdate loc = new EQRLocationUpdate(
				EQRLocationUpdate.AMBULANCE_LOCATION, myAgent.getAID());
		loc.setIsMoving(false);
		loc.setIsDead(false);
		loc.setCurrent(details.getLocation());
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
		if (ed instanceof AmbulanceDetails) {
			EQRLogger.log(
					logger,
					null,
					myAgent.getLocalName(),
					"Location changed to "
							+ ((AmbulanceDetails) ed).getCurrentLocation());
			EQRLocationUpdate loc = new EQRLocationUpdate(
					EQRLocationUpdate.AMBULANCE_LOCATION, myAgent.getAID());
			loc.setIsMoving(!((AmbulanceDetails) ed).getArrived());
			loc.setIsDead(false);
			loc.setCurrent(((AmbulanceDetails) ed).getCurrentLocation());
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
					+ ((AmbulanceDetails) ed).getCurrentLocation());
		}
	}

}
