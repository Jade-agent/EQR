package org.nkigen.eqr.emergencycontrol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jade.util.Logger;

import org.nkigen.eqr.agents.EQRAgentsHelper;
import org.nkigen.eqr.agents.EmergencyControlCenterAgent;
import org.nkigen.eqr.agents.behaviours.EmergencyControlBehaviour;
import org.nkigen.eqr.common.EmergencyResponseBase;
import org.nkigen.eqr.logs.EQRLogger;
import org.nkigen.eqr.messages.AmbulanceNotifyMessage;
import org.nkigen.eqr.messages.EQRRoutingError;
import org.nkigen.eqr.messages.EQRRoutingResult;
import org.nkigen.eqr.messages.MultipleRoutingRequestMessage;
import org.nkigen.eqr.messages.MultipleRoutingResponseMessage;
import org.nkigen.eqr.patients.PatientDetails;

import com.sun.corba.se.impl.orbutil.concurrent.Mutex;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;
import jade.lang.acl.MessageTemplate.MatchExpression;

public class AssignAmbulanceBehaviour extends SimpleBehaviour {

	public static final String AMBULANCE_ROUTER_ID = "ambulance_router_msg";
	boolean done = false;
	boolean req_r = false; /* Not yet made a request to the router */
	PatientDetails patient;
	List<EmergencyResponseBase> bases;
	AID router;
	EmergencyControlBehaviour ecb;
	Logger logger;
	/* Assumption: This list of bases contain atleast an ambulance */
	public AssignAmbulanceBehaviour(Agent a, PatientDetails p,
			List<EmergencyResponseBase> bases, EmergencyControlBehaviour ecb) {
		super(a);
		this.ecb = ecb;
		patient = p;
		this.bases = bases;
		router = EQRAgentsHelper.locateRoutingServer(myAgent);
		logger = EQRLogger.prep(logger,myAgent.getLocalName());
	}

	@Override
	public void action() {
		while (router == null)
			router = EQRAgentsHelper.locateRoutingServer(myAgent);
		
		MessageTemplate template = MessageTemplate.and(MessageTemplate.MatchSender(router),MessageTemplate.MatchConversationId(AMBULANCE_ROUTER_ID));
		ACLMessage msg = myAgent.receive(template);

		if (msg == null && !req_r) {
			MultipleRoutingRequestMessage req = new MultipleRoutingRequestMessage();
			req.setBases((ArrayList<EmergencyResponseBase>) bases);
			req.setReply_to(myAgent.getAID());
		
			req.setTo(patient.getLocation());

			ACLMessage to_send = new ACLMessage(ACLMessage.REQUEST);
			to_send.addReceiver(router);
			to_send.setConversationId(AMBULANCE_ROUTER_ID);
			try {
				to_send.setContentObject(req);
				EQRLogger.log(logger, to_send, myAgent.getLocalName(), getBehaviourName()+";Message sent to the Router:"+ router.getLocalName());
				myAgent.send(to_send);
				req_r = true;
				System.out.println(getBehaviourName()
						+ " ASSIGN AMBULANCE REQUEST SENT TO ROUTER "
						+ patient.getAID());
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
						EQRLogger.log(logger, msg, myAgent.getLocalName(), getBehaviourName()+": Message received from router "+ router.getLocalName());
						System.out.println(getBehaviourName()
								+ " route found for patient-ambulance");
						informPatientAndAmbulance((MultipleRoutingResponseMessage) content);
						done = true;
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
				.log(logger,
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
				EQRLogger.log(logger, to_patient, myAgent.getLocalName(), getBehaviourName()+"; Routing error encountered");
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
		AmbulanceNotifyMessage anm = new AmbulanceNotifyMessage(
				AmbulanceNotifyMessage.NOTIFY_PATIENT_ROUTE);
		anm.setPatient(patient);
		anm.setResult(res);
		EQRLogger.log(logger, null, myAgent.getLocalName(), getBehaviourName()+"; BASE SIZE: "+bases.get(0).getLocation()+ " msg: "+msg.getBase().getLocation());
		try {
			to_ambulance.setContentObject(anm);
			for (EmergencyResponseBase b : bases) {
				if (isSameBase(b, msg.getBase())) {
					System.out
							.println(getBehaviourName() + "BASES ARE EQUAL: ");
					to_ambulance.addReceiver(b.assignAmbulance());
					ecb.unlockAmbulanceQueue();
					EQRLogger.log(logger, null, myAgent.getLocalName(), getBehaviourName()+"; Ambulance queue unlocked");
				}
			}
			myAgent.send(to_ambulance);
			
			to_patient.addReceiver(patient.getAID());
			EQRLogger.log(logger, to_ambulance, myAgent.getLocalName(), getBehaviourName()+";Message sent to ambulance");
			EQRLogger.log(logger, to_patient, myAgent.getLocalName(), getBehaviourName()+";Message sent to Patient");
			/* Add more useful info here for patient */
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
