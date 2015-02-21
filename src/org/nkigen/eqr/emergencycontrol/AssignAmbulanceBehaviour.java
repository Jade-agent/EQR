package org.nkigen.eqr.emergencycontrol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.nkigen.eqr.agents.EQRAgentsHelper;
import org.nkigen.eqr.common.EmergencyResponseBase;
import org.nkigen.eqr.messages.MultipleRoutingRequestMessage;
import org.nkigen.eqr.messages.MultipleRoutingResponseMessage;
import org.nkigen.eqr.patients.PatientDetails;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class AssignAmbulanceBehaviour extends SimpleBehaviour{

	boolean done = false;
	boolean req_r = false; /*Not yet made a request to the router*/
	PatientDetails patient;
	List<EmergencyResponseBase> bases;
	AID router;
	/*Assumbtion: This list of bases contain atleast an ambulance*/
	public AssignAmbulanceBehaviour(Agent a, PatientDetails p, List<EmergencyResponseBase> bases) {
		super(a);
		patient = p;
		this.bases = bases;
	}
	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		
		if(msg == null && !req_r){
			MultipleRoutingRequestMessage req = new MultipleRoutingRequestMessage();
			req.setBases((ArrayList<EmergencyResponseBase>) bases);
			req.setReply_to(myAgent.getAID());
			req.setTo(patient.getLocation());
			
			if(router == null)
				router = EQRAgentsHelper.locateRoutingServer(myAgent);
			ACLMessage to_send = new ACLMessage(ACLMessage.REQUEST);
			to_send.addReceiver(router);
			try {
				to_send.setContentObject(req);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			myAgent.send(to_send);
			req_r = true;
		}
		else{
			Object content = null;
			switch(msg.getPerformative()){
			case ACLMessage.INFORM:
				
				try {
					content = msg.getContentObject();
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(content instanceof MultipleRoutingResponseMessage){
					System.out.println(getBehaviourName()+ " route found for patient-ambulance");
					informPatientAndAmbulance((MultipleRoutingResponseMessage) content);
				}
				break;
			}
		}
	}

	private void informPatientAndAmbulance(MultipleRoutingResponseMessage msg){
	/*TODO*/	
	}
	
	@Override
	public boolean done() {
		return done;
	}

}
