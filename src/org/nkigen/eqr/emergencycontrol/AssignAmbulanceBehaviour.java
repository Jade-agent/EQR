package org.nkigen.eqr.emergencycontrol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.nkigen.eqr.agents.EQRAgentsHelper;
import org.nkigen.eqr.common.EmergencyResponseBase;
import org.nkigen.eqr.messages.AmbulanceNotifyMessage;
import org.nkigen.eqr.messages.EQRRoutingError;
import org.nkigen.eqr.messages.EQRRoutingResult;
import org.nkigen.eqr.messages.MultipleRoutingRequestMessage;
import org.nkigen.eqr.messages.MultipleRoutingResponseMessage;
import org.nkigen.eqr.patients.PatientDetails;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class AssignAmbulanceBehaviour extends SimpleBehaviour {

	boolean done = false;
	boolean req_r = false; /* Not yet made a request to the router */
	PatientDetails patient;
	List<EmergencyResponseBase> bases;
	AID router;

	/* Assumbtion: This list of bases contain atleast an ambulance */
	public AssignAmbulanceBehaviour(Agent a, PatientDetails p,
			List<EmergencyResponseBase> bases) {
		super(a);
		patient = p;
		this.bases = bases;
	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();

		if (msg == null && !req_r) {
			MultipleRoutingRequestMessage req = new MultipleRoutingRequestMessage();
			req.setBases((ArrayList<EmergencyResponseBase>) bases);
			req.setReply_to(myAgent.getAID());
			req.setTo(patient.getLocation());

			if (router == null)
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
		} else {
			Object content = null;
			switch (msg.getPerformative()) {
			case ACLMessage.INFORM:

				try {
					content = msg.getContentObject();
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (content instanceof MultipleRoutingResponseMessage) {
					System.out.println(getBehaviourName()
							+ " route found for patient-ambulance");
					informPatientAndAmbulance((MultipleRoutingResponseMessage) content);
				}
				break;
			}
		}
	}

	private void informPatientAndAmbulance(MultipleRoutingResponseMessage msg) {
		/* TODO */
		EQRRoutingResult res = msg.getResult();
		
		ACLMessage to_patient = null;
		ACLMessage to_ambulance = null;
		if (res instanceof EQRRoutingError) {
			to_patient = new ACLMessage(ACLMessage.FAILURE);
			to_patient.addReceiver(patient.getAID());
			try {
				to_patient.setContentObject(res);
				/* TODO: Put some useful message here */
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			myAgent.send(to_patient);
			return;
		}
		to_ambulance = new ACLMessage(ACLMessage.INFORM);
		to_patient = new ACLMessage(ACLMessage.INFORM);
		AmbulanceNotifyMessage anm = new AmbulanceNotifyMessage(AmbulanceNotifyMessage.NOTIFY_PATIENT_ROUTE);
		anm.setPatient(patient);
		anm.setResult(res);
		try {
			to_ambulance.setContentObject(anm);
			to_ambulance.addReceiver(msg.getBase().assignAmbulance());
			myAgent.send(to_ambulance);
			to_patient.addReceiver(patient.getAID());
			/*Add more useful info here for patient*/
			myAgent.send(to_patient);
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