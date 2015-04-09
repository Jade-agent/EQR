package org.nkigen.eqr.ambulance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jade.util.Logger;

import org.nkigen.eqr.agents.EQRAgentsHelper;
import org.nkigen.eqr.common.EmergencyResponseBase;
import org.nkigen.eqr.logs.EQRLogger;
import org.nkigen.eqr.messages.EQRRoutingError;
import org.nkigen.eqr.messages.EQRRoutingResult;
import org.nkigen.eqr.messages.HospitalArrivalMessage;
import org.nkigen.eqr.messages.HospitalRequestMessage;
import org.nkigen.eqr.messages.TrafficUpdateMessage;
import org.nkigen.eqr.patients.PatientDetails;
import org.nkigen.maps.routing.EQRPoint;
import org.nkigen.maps.routing.graphhopper.EQRGraphHopperResult;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class AmbulanceNearestHospitalBehaviour extends SimpleBehaviour {

	boolean done = false;
	AmbulanceDetails ambulance;
	PatientDetails patient;
	Logger logger;
	AID command_center = null;
	boolean req_made = false;
	TrafficUpdateMessage traffic;
	public AmbulanceNearestHospitalBehaviour(Agent agent,
			PatientDetails patient, AmbulanceDetails ambulance, TrafficUpdateMessage traffic) {
		super(agent);
		this.ambulance = ambulance;
		this.patient = patient;
		this.traffic = traffic;
		logger = EQRLogger.prep(logger, myAgent.getLocalName());
		command_center = EQRAgentsHelper.locateControlCenter(myAgent);
	}

	@Override
	public void action() {
		while (command_center == null)
			command_center = EQRAgentsHelper.locateControlCenter(myAgent);
		MessageTemplate temp = MessageTemplate.MatchSender(command_center);
		
		ACLMessage msg = myAgent.receive(temp);

		if (msg == null && !req_made) {
			EQRLogger.log(logger, msg, myAgent.getLocalName(),
					getBehaviourName() + ":Sending request to Command Center");
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
				EQRLogger.log(logger, msg, myAgent.getLocalName(),
						getBehaviourName()
								+ ":Sending request to Command Center");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else if (msg != null && req_made) {
			EQRLogger.log(logger, msg, myAgent.getLocalName(),
					getBehaviourName() + ":Command Center Response received");
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
		} else {
			if (msg != null) {
				EQRLogger
						.log(EQRLogger.LOG_EERROR,
								logger,
								msg,
								myAgent.getLocalName(),
								getBehaviourName()
										+ ": Should not happen, wrong message received ");
				myAgent.send(msg);
				done = true;
			}
		}

	}

	private void handleToHospital(HospitalRequestMessage msg) {
		Object[] data = ((HospitalRequestMessage) msg).getMessage();
		EQRLogger.log(logger, null, myAgent.getLocalName(), getBehaviourName()
				+ ":Received route to hospital");
		if (data.length == 2) {
			EmergencyResponseBase base = (EmergencyResponseBase) data[0];
			EQRRoutingResult route = (EQRRoutingResult) data[1];
			if (route instanceof EQRRoutingError) {
				EQRLogger.log(EQRLogger.LOG_EERROR, logger, null,
						myAgent.getLocalName(), getBehaviourName()
								+ ": No route to hospital found!!");
				System.out.println(myAgent.getLocalName()
						+ " ERROR: cant find route to hospital");
			} else {
				List<EQRPoint> points = ((EQRGraphHopperResult) route)
						.getPoints();

				long duration = ((EQRGraphHopperResult) route).getDuration();
				double distance = ((EQRGraphHopperResult) route).getDistance();
				double delay = 0;
				double rate = 1;
				if (traffic != null) {
					delay = traffic.getDelay();
					rate = traffic.getSimulationRate();
				}

				double sim_speed = distance / (duration * rate);
				EQRLogger.log(logger, null, myAgent.getLocalName(),
						getBehaviourName() + ": Route of duration: " + duration
								+ " and points :" + points.size() + " found ");
				MessageTemplate traffic_temp = MessageTemplate.MatchConversationId(TrafficUpdateMessage.TRAFFIC_SUBSCRIBERS_CONV);
				for (int i = 0; i < points.size() - 1; i++) {

					ACLMessage up = myAgent.receive(traffic_temp);
					if (up != null) {
						try {
							Object content = up.getContentObject();
							if (content instanceof TrafficUpdateMessage) {
								traffic = (TrafficUpdateMessage) content;
								delay = traffic.getDelay();
								rate = traffic.getSimulationRate();
								sim_speed = distance / (duration * rate);
							}

						} catch (UnreadableException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					EQRPoint curr = points.get(i);
					EQRPoint nxt = points.get(i + 1);
					ambulance.setCurrentLocation(curr);
					double dist = EQRAgentsHelper.getDistanceFromGPSCood(curr, nxt);
					double st = (dist / sim_speed) + delay * rate;
					try {
						Thread.sleep((long) st);

					} catch (InterruptedException e) {

						e.printStackTrace();
					}
				}
				ambulance.setCurrentLocation(points.get(points.size() - 1));
				EQRLogger.log(
						logger,
						null,
						myAgent.getLocalName(),
						getBehaviourName() + ": Patient "
								+ patient.getAID().getLocalName()
								+ " arrived safely to hospital at "
								+ base.getLocation());
				System.out.println(myAgent.getLocalName()
						+ " Patient arrived safely at hospital "
						+ patient.getAID() + " BASE: " + base.getLocation());
				HospitalArrivalMessage ham = new HospitalArrivalMessage();
				ham.setPatient(patient);
				ACLMessage notify = new ACLMessage(ACLMessage.INFORM);
				notify.addReceiver(patient.getAID());
				notify.addReceiver(myAgent.getAID());
				notify.addReceiver(base.getAvailable().get(0));
				try {
					notify.setContentObject(ham);
					myAgent.send(notify);
					EQRLogger.log(logger, notify, myAgent.getLocalName(),
							getBehaviourName() + "Message sent");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		done = true;
	}

	@Override
	public boolean done() {
		return done;
	}

}
