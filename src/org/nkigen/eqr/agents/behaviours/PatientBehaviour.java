package org.nkigen.eqr.agents.behaviours;

import java.io.IOException;

import org.nkigen.eqr.agents.EQRAgentsHelper;
import org.nkigen.eqr.common.EmergencyDetails;
import org.nkigen.eqr.common.EmergencyStateChangeInitiator;
import org.nkigen.eqr.common.EmergencyStateChangeListener;
import org.nkigen.eqr.common.EmergencyStatus;
import org.nkigen.eqr.fires.RequestFireBehaviour;
import org.nkigen.eqr.logs.EQRLogger;
import org.nkigen.eqr.messages.ChangeEmergencyStatusMessage;
import org.nkigen.eqr.messages.EQRLocationUpdate;
import org.nkigen.eqr.messages.HospitalArrivalMessage;
import org.nkigen.eqr.messages.PatientInitMessage;
import org.nkigen.eqr.messages.PickPatientMessage;
import org.nkigen.eqr.patients.PatientDetails;
import org.nkigen.eqr.patients.PatientGoals;
import org.nkigen.eqr.patients.RequestAmbulanceBehaviour;
import org.nkigen.maps.viewer.updates.EQRStatusPanelItem;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.util.Logger;

public class PatientBehaviour extends CyclicBehaviour implements
		EmergencyStateChangeListener {

	PatientDetails patient = null;
	PatientGoals goals;
	EmergencyStateChangeInitiator listener;
	Logger logger;
	public PatientBehaviour(Agent agent) {
		super(agent);
		listener = new EmergencyStateChangeInitiator();
		listener.addListener(this);
		goals = new PatientGoals();
		goals.newGoal(PatientGoals.REQUEST_AMBULANCE_PICKUP,
				RequestAmbulanceBehaviour.class);
		logger = EQRLogger.prep(logger, myAgent.getLocalName());
		System.out.println(getClass().getName() + " Stated");
	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		if (msg == null) {
			block();
			return;
		}
		EQRLogger.log(logger, msg, myAgent.getLocalName(),"Message recevied");
		handle(msg);

	}

	private void handle(ACLMessage msg) {
		try {
			Object content = msg.getContentObject();
			if (content instanceof PatientInitMessage) {
				System.out
						.println(getClass().getName() + " Init Message recvd");
				if (patient == null) {
					patient = ((PatientInitMessage) content).getPatient();
					patient.setListener(listener);
					System.out.println(getClass().getName()
							+ " Patient Initialized");
				}
			} else if (content instanceof PickPatientMessage) {
				System.out.println(myAgent.getLocalName()
						+ " About to be picked up. Yaaay!");
				patient.setStatus(EmergencyStatus.PATIENT_PICKED);

			}
			else if(content instanceof HospitalArrivalMessage){
				System.out.println(myAgent.getLocalName()
						+ " Patient arrived at the hospital !!!");
				patient.setStatus(EmergencyStatus.PATIENT_DELIVERED);
			}
			else if (content instanceof ChangeEmergencyStatusMessage) {
				if (((ChangeEmergencyStatusMessage) content).getType() == ChangeEmergencyStatusMessage.TYPE_PATIENT)
					patient.setStatus(((ChangeEmergencyStatusMessage) content)
							.getStatus());

			}
			else{
				System.out.println(myAgent.getLocalName()+" Message recv but not understood");
			}
		} catch (UnreadableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendWaitPatientUpdate(int status){
		System.out.println(getBehaviourName()+": "+myAgent.getLocalName()+" Patient sending location Update");
		EQRLogger.log(logger, null, myAgent.getLocalName(), "Patient Status Update");
		AID update = EQRAgentsHelper.locateUpdateServer(myAgent);
		EQRLocationUpdate loc = new EQRLocationUpdate(EQRLocationUpdate.PATIENT_LOCATION, myAgent.getAID());
		loc.setStatus(status);
		loc.setIsMoving(false);
		loc.setIsDead(false);
		loc.setCurrent(patient.getLocation());
		ACLMessage msg = new ACLMessage(ACLMessage.PROPAGATE);
		msg.addReceiver(update);
		try {
			msg.setContentObject(loc);
			EQRLogger.log(logger, msg, myAgent.getLocalName(), "Message sent");
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
		if (ed instanceof PatientDetails) {// && ed.getAID() == myAgent.getAID()
			System.out.println(getClass().getName() + " On State Change "
					+ ed.getAID() + " myAID " + myAgent.getAID());
			switch (((PatientDetails) ed).getStatus()) {
			case EmergencyStatus.PATIENT_WAITING:
				params[0] = myAgent;
				params[1] = patient;
				behaviour = goals.executePlan(
						PatientGoals.REQUEST_AMBULANCE_PICKUP, params);
				sendWaitPatientUpdate(EmergencyStatus.PATIENT_WAITING);
				break;
			case EmergencyStatus.PATIENT_PICKED:
				sendWaitPatientUpdate(EmergencyStatus.PATIENT_PICKED);
				/*TODO: Log this*/
				break;
			case EmergencyStatus.PATIENT_DELIVERED:
				sendWaitPatientUpdate(EmergencyStatus.PATIENT_DELIVERED);
				break;
			case EmergencyStatus.PATIENT_DEAD:
				sendWaitPatientUpdate(EmergencyStatus.PATIENT_DEAD);
				break;
			default:
				EQRLogger.log(EQRLogger.LOG_EERROR,logger, null, myAgent.getLocalName(), "Wrong Patient Status set");
				System.out.println(getClass().getName()
						+ " Status not understood");

			}

			if (behaviour != null) {
				myAgent.addBehaviour(behaviour);
			} else {
				System.out.println(getClass().getName()
						+ " behaviour returned null");
			}
		}

	}

	private class PatientDyingTimer extends WakerBehaviour{

		public PatientDyingTimer(Agent a, long timeout) {
			super(a, timeout);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void onWake(){
			System.out.println(getBehaviourName()+" Deadline Reached"+ myAgent.getLocalName()+ " is Dying Now");
			PatientBehaviour.this.patient.setStatus(EmergencyStatus.PATIENT_DEAD);
		}
		
		
	}
}
