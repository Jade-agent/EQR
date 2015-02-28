package org.nkigen.eqr.emergencycontrol;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.nkigen.eqr.agents.EQRAgentsHelper;
import org.nkigen.eqr.ambulance.AmbulanceDetails;
import org.nkigen.eqr.common.EmergencyResponseBase;
import org.nkigen.eqr.messages.AmbulanceNotifyMessage;
import org.nkigen.eqr.messages.EQRRoutingError;
import org.nkigen.eqr.messages.EQRRoutingResult;
import org.nkigen.eqr.messages.HospitalRequestMessage;
import org.nkigen.eqr.messages.MultipleRoutingRequestMessage;
import org.nkigen.eqr.messages.MultipleRoutingResponseMessage;
import org.nkigen.eqr.patients.PatientDetails;
import org.nkigen.maps.routing.graphhopper.EQRGraphHopperResult;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class NearestHospitalBehaviour extends SimpleBehaviour {

	boolean done = false;
	boolean req_r = false; /* Not yet made a request to the router */
	AmbulanceDetails ambulance;
	List<EmergencyResponseBase> hospitals;
	AID router;

	/* Assumption: This list of bases contain atleast an ambulance */
	public NearestHospitalBehaviour(Agent a, AmbulanceDetails p,
			List<EmergencyResponseBase> bases) {
		super(a);
		ambulance = p;
		this.hospitals = bases;
	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();

		if (msg == null && !req_r) {
			MultipleRoutingRequestMessage req = new MultipleRoutingRequestMessage();
			req.setBases((ArrayList<EmergencyResponseBase>) hospitals);
			req.setReply_to(myAgent.getAID());
			req.setTo(ambulance.getCurrentLocation());

			if (router == null)
				router = EQRAgentsHelper.locateRoutingServer(myAgent);
			ACLMessage to_send = new ACLMessage(ACLMessage.REQUEST);
			to_send.addReceiver(router);
			try {
				to_send.setContentObject(req);
				myAgent.send(to_send);
				req_r = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if(msg!= null && req_r){
			Object content = null;
			switch (msg.getPerformative()) {
			case ACLMessage.INFORM:

				try {
					content = msg.getContentObject();
					if (content instanceof MultipleRoutingResponseMessage) {
						System.out.println(getBehaviourName()
								+ " route found for hospital-ambulance");
						informAmbulance((MultipleRoutingResponseMessage) content);
					}
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			}
		}
	}

	private void informAmbulance(MultipleRoutingResponseMessage msg) {
		/* TODO */
		EQRRoutingResult res = msg.getResult();

		ACLMessage to_ambulance = null;
		if (res instanceof EQRRoutingError) {
			/* TODO: Inform Ambulance no route */
			System.out.println(getBehaviourName()
					+ " error informAmbulance no route found");
			done = true;
			return;
		}
		to_ambulance = new ACLMessage(ACLMessage.INFORM);

		// AmbulanceNotifyMessage anm = new
		// AmbulanceNotifyMessage(AmbulanceNotifyMessage.NOTIFY_HOSPITAL_ROUTE);
		HospitalRequestMessage anm = new HospitalRequestMessage(
				HospitalRequestMessage.HOSPITAL_REPLY);
		Object[] d = new Object[2];
		d[0] = msg.getBase();
		res = ((EQRGraphHopperResult)res).reverse();
		d[1] = res;
		//anm.setHospital(msg.getBase());
		//anm.setResult(res);
		anm.setMessage(d);
		try {
			to_ambulance.setContentObject(anm);
			to_ambulance.addReceiver(ambulance.getAID());
			myAgent.send(to_ambulance);

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
