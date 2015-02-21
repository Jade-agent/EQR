package org.nkigen.eqr.agents.behaviours;

import org.nkigen.eqr.common.EmergencyDetails;
import org.nkigen.eqr.common.EmergencyStateChangeInitiator;
import org.nkigen.eqr.common.EmergencyStateChangeListener;
import org.nkigen.eqr.common.EmergencyStatus;
import org.nkigen.eqr.fires.RequestFireBehaviour;
import org.nkigen.eqr.messages.PatientInitMessage;
import org.nkigen.eqr.patients.PatientDetails;
import org.nkigen.eqr.patients.PatientGoals;
import org.nkigen.eqr.patients.RequestAmbulanceBehaviour;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class PatientBehaviour extends CyclicBehaviour implements EmergencyStateChangeListener {

	PatientDetails patient = null;
	PatientGoals goals;

	public PatientBehaviour(Agent agent) {
		super(agent);
		patient.setAID(myAgent.getAID());
		 EmergencyStateChangeInitiator.getInstance().addListener(this);
		goals = new PatientGoals();
		goals.newGoal(PatientGoals.REQUEST_AMBULANCE_PICKUP, RequestAmbulanceBehaviour.class);
		System.out.println(getClass().getName()+" Stated");
	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		if(msg == null){
			block();
			return;
		}
		handle(msg);
		
	}

	private void handle(ACLMessage msg){
		try {
			Object content = msg.getContentObject();
			if(content instanceof PatientInitMessage){
				System.out.println(getClass().getName()+" Init Message recvd");
				if(patient == null){
					patient = ((PatientInitMessage) content).getPatient();
					System.out.println(getClass().getName()+" Patient Initialized");
				}
			}
		} catch (UnreadableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void onEmergencyStateChange(EmergencyDetails ed) {
		// TODO Auto-generated method stub
		System.out.println(getClass().getName()+" On State Change");
		Object[] params = new Object[2];
		Behaviour behaviour = null;
		if(ed instanceof PatientDetails && ed.getAID() == myAgent.getAID()){
			
			switch(((PatientDetails) ed).getStatus()){
			case EmergencyStatus.PATIENT_WAITING:
				params[0] = myAgent;
				params[1] = patient;
				behaviour = goals.executePlan(PatientGoals.REQUEST_AMBULANCE_PICKUP, params);
				break;
			case EmergencyStatus.PATIENT_PICKED:
				break;
			case EmergencyStatus.PATIENT_DELIVERED:
				break;
			case EmergencyStatus.PATIENT_DEAD:
				break;
				default:
					System.out.println(getClass().getName()+" Status not understood");
				
			}
			
			if(behaviour != null){
				myAgent.addBehaviour(behaviour);
			}
			else{
				System.out.println(getClass().getName()+ " behaviour returned null");
			}
		}
		
	}

}
