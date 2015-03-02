package org.nkigen.eqr.agents.behaviours;

import java.io.IOException;
import jade.util.Logger;

import org.nkigen.eqr.agents.EQRAgentsHelper;
import org.nkigen.eqr.hospital.HospitalDetails;
import org.nkigen.eqr.logs.EQRLogger;
import org.nkigen.eqr.messages.EQRLocationUpdate;
import org.nkigen.eqr.messages.FireInitMessage;
import org.nkigen.eqr.messages.HospitalArrivalMessage;
import org.nkigen.eqr.messages.HospitalInitMessage;
import org.nkigen.maps.viewer.updates.EQRStatusPanelItem;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class HospitalBehaviour extends CyclicBehaviour {

	HospitalDetails details;
	AID update;
	boolean is_location_shown = false;
	Logger logger;
	public HospitalBehaviour(Agent agent) {
		super(agent);
		AID update = EQRAgentsHelper.locateUpdateServer(myAgent);
		logger = EQRLogger.prep(logger, myAgent.getLocalName());
	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();

		if (msg != null) {
			EQRLogger.log(logger, msg, myAgent.getLocalName(),
					getBehaviourName() + ": Message Received");
			switch (msg.getPerformative()) {
			case ACLMessage.INFORM:
				try {
					Object content = msg.getContentObject();
					if (content instanceof HospitalInitMessage) {
						details = ((HospitalInitMessage) content).getHospital();
						sendPostionUpdate();
					} else if (content instanceof HospitalArrivalMessage) {
						System.out.println(myAgent.getLocalName()
								+ " Hospital has received patient: "
								+ ((HospitalArrivalMessage) content)
										.getPatient().getAID());
					}
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}
		else {
			block();
		}

	}
	private void sendPostionUpdate(){
		while(update == null)
			update  = EQRAgentsHelper.locateUpdateServer(myAgent);
		EQRLogger.log(logger, null, myAgent.getLocalName(),
				" Initial location at " + details.getLocation());
		System.out.println(getBehaviourName()+": "+myAgent.getLocalName()+" Hospital sending init location");
		EQRLocationUpdate loc = new EQRLocationUpdate(EQRLocationUpdate.HOSPITAL_LOCATION, myAgent.getAID());
		loc.setIsMoving(false);
		loc.setIsDead(false);
		loc.setCurrent(details.getLocation());
		ACLMessage msg = new ACLMessage(ACLMessage.PROPAGATE);
		msg.addReceiver(update);
		try {
			msg.setContentObject(loc);
			myAgent.send(msg);
			EQRLogger.log(logger, null, myAgent.getLocalName()," message sent");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
