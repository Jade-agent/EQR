package org.nkigen.eqr.agents.behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import org.nkigen.eqr.ambulance.AmbulanceDetails;
import org.nkigen.eqr.ambulance.AmbulanceGoals;
import org.nkigen.eqr.common.EmergencyDetails;
import org.nkigen.eqr.common.EmergencyStateChangeInitiator;
import org.nkigen.eqr.common.EmergencyStateChangeListener;
import org.nkigen.eqr.messages.AmbulanceInitMessage;
import org.nkigen.eqr.messages.AmbulanceNotifyMessage;
import org.nkigen.eqr.messages.HospitalArrivalMessage;
import org.nkigen.eqr.messages.PickPatientMessage;
import org.nkigen.eqr.models.EQREmergencyPoint;

public class AmbulanceBehaviour extends CyclicBehaviour implements
		EmergencyStateChangeListener {

	AmbulanceGoals goals;
	AmbulanceDetails details;
	EmergencyStateChangeInitiator listener;

	public AmbulanceBehaviour(Agent agent) {
		super(agent);
		listener = new EmergencyStateChangeInitiator();
		listener.addListener(this);
		goals = new AmbulanceGoals();
	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		if (msg != null) {
			switch (msg.getPerformative()) {
			case ACLMessage.INFORM:
				try {
					Object content = msg.getContentObject();
					if (content instanceof AmbulanceNotifyMessage) {
						System.out.println(getBehaviourName() + " "
								+ myAgent.getLocalName()
								+ " recv notification msg");
						Object[] params = new Object[3];
						params[0] = myAgent;
						params[1] = (AmbulanceNotifyMessage) content;
						params[2] = details;

						Behaviour b = goals.executePlan(
								AmbulanceGoals.PICK_PATIENT, params);
						if (b != null)
							myAgent.addBehaviour(b);
					} else if (content instanceof AmbulanceInitMessage) {
						details = ((AmbulanceInitMessage) content)
								.getAmbulance();
						details.setListener(listener);
					} else if (content instanceof PickPatientMessage) {
						System.out
								.println(myAgent.getLocalName()
										+ " : Trying to find the neareset hospital for "
										+ ((PickPatientMessage) content)
												.getPatient().getAID());
						Object[] params = new Object[3];
						params[0] = myAgent;
						params[1] = ((PickPatientMessage) content).getPatient();
						params[2] = details;
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
						Object[] params = new Object[2];
						params[0] = myAgent;
						params[1] = details;
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

	@Override
	public void onEmergencyStateChange(EmergencyDetails ed) {
		// TODO Auto-generated method stub
		if (ed instanceof AmbulanceDetails) {
			System.out.println(getBehaviourName() + " "
					+ myAgent.getLocalName() + " Location changed to "
					+ ((AmbulanceDetails) ed).getCurrentLocation());
		}
	}

}
