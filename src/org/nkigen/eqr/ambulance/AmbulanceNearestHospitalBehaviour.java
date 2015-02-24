package org.nkigen.eqr.ambulance;

import java.io.IOException;

import org.nkigen.eqr.agents.EQRAgentsHelper;
import org.nkigen.eqr.common.EmergencyResponseBase;
import org.nkigen.eqr.messages.EQRRoutingResult;
import org.nkigen.eqr.messages.HospitalRequestMessage;
import org.nkigen.eqr.patients.PatientDetails;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class AmbulanceNearestHospitalBehaviour extends SimpleBehaviour {

	boolean done = false;
	AmbulanceDetails ambulance;
	PatientDetails patient;
	AID command_center = null;
	boolean req_made = false;

	public AmbulanceNearestHospitalBehaviour(Agent agent,
			PatientDetails patient, AmbulanceDetails ambulance) {
		super(agent);
		this.ambulance = ambulance;
		this.patient = patient;
		command_center = EQRAgentsHelper.locateControlCenter(myAgent);
	}

	@Override
	public void action() {
		if (command_center == null)
			command_center = EQRAgentsHelper.locateControlCenter(myAgent);
		ACLMessage msg = myAgent.receive();
		if (msg == null && !req_made) {
			msg = new ACLMessage(ACLMessage.REQUEST);
			msg.addReceiver(command_center);
			HospitalRequestMessage hrm = new HospitalRequestMessage(
					HospitalRequestMessage.HOSPITAL_REQUEST);
			Object[] data = new Object[1];
			data[0] = ambulance;
			hrm.setMessage(data);
			try {
				msg.setContentObject(hrm);
				myAgent.send(msg);
				req_made = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (msg != null && req_made) {
			switch (msg.getPerformative()) {
			case ACLMessage.INFORM:
				try {
					Object content = msg.getContentObject();
					if (content instanceof HospitalRequestMessage) {
						if (((HospitalRequestMessage) content).getType() == HospitalRequestMessage.HOSPITAL_REPLY) {
							handleToHospital((HospitalRequestMessage) content);

						}
					}
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}

	}

	private void handleToHospital(HospitalRequestMessage msg) {
		Object[] data = ((HospitalRequestMessage) msg).getMessage();
		if (data.length == 2) {
			EmergencyResponseBase base = (EmergencyResponseBase) data[0];
			EQRRoutingResult route = (EQRRoutingResult) data[1];
			System.out.println(myAgent.getLocalName()
					+ ": Response Base found to be: " + base.getLocation());
		}
		done = true;
	}

	@Override
	public boolean done() {
		return done;
	}

}
