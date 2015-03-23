package org.nkigen.eqr.fireengine;

import java.io.IOException;
import java.util.List;

import jade.util.Logger;

import org.nkigen.eqr.agents.EQRAgentsHelper;
import org.nkigen.eqr.logs.EQRLogger;
import org.nkigen.eqr.messages.BaseRouteMessage;
import org.nkigen.eqr.messages.EQRRoutingCriteria;
import org.nkigen.eqr.messages.EQRRoutingError;
import org.nkigen.eqr.messages.EQRRoutingResult;
import org.nkigen.eqr.messages.HospitalRequestMessage;
import org.nkigen.eqr.messages.MissionCompleteNotificaton;
import org.nkigen.eqr.messages.TrafficUpdateMessage;
import org.nkigen.maps.routing.EQRPoint;
import org.nkigen.maps.routing.graphhopper.EQRGraphHopperResult;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class EngineBackToBase extends SimpleBehaviour {

	boolean done = false;
	FireEngineDetails engine;
	AID command_center = null;
	boolean req_made = false;
	Logger logger;
	TrafficUpdateMessage traffic;
	public EngineBackToBase(Agent agent, FireEngineDetails engine, TrafficUpdateMessage traffic) {
		super(agent);
		this.engine = engine;
		this.traffic = traffic;
		command_center = EQRAgentsHelper.locateControlCenter(myAgent);
		logger = EQRLogger.prep(logger, myAgent.getLocalName());
	}

	@Override
	public void action() {
		while (command_center == null)
			command_center = EQRAgentsHelper.locateControlCenter(myAgent);
		MessageTemplate temp = MessageTemplate.MatchSender(command_center);
		ACLMessage msg = myAgent.receive(temp);
		if (msg == null && !req_made) {
			msg = new ACLMessage(ACLMessage.REQUEST);
			msg.addReceiver(command_center);
			BaseRouteMessage brm = new BaseRouteMessage(
					BaseRouteMessage.REQUEST);
			EQRRoutingCriteria criteria = new EQRRoutingCriteria(
					engine.getCurrentLocation(), engine.getLocation());
			brm.setCriteria(criteria);
			try {
				msg.setContentObject(brm);
				myAgent.send(msg);
				req_made = true;
				EQRLogger.log(logger, msg, myAgent.getLocalName(),
						getBehaviourName() + ":Sending request to Command Center");
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
					if (content instanceof BaseRouteMessage) {
						if (((BaseRouteMessage) content).getType() == BaseRouteMessage.REPLY) {
							goToBase((BaseRouteMessage) content);
							done = true;
						} else {
							System.out.println(myAgent.getLocalName()
									+ " ERROR BackToBase Message");
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
				myAgent.send(msg);
				EQRLogger
				.log(EQRLogger.LOG_EERROR,
						logger,
						msg,
						myAgent.getLocalName(),
						getBehaviourName()
								+ ": Should not happen, wrong message received ");
				done = true;
			}
		}
	}

	private void goToBase(BaseRouteMessage msg) {
		EQRLogger.log(logger, null, myAgent.getLocalName(), getBehaviourName()
				+ ":Received route to Base");
		EQRRoutingResult route = msg.getResult();
		if (route instanceof EQRRoutingError) {
			EQRLogger.log(EQRLogger.LOG_EERROR, logger, null,
					myAgent.getLocalName(), getBehaviourName()
							+ ": No route to Base found!!");
			return;
		}

		List<EQRPoint> points = ((EQRGraphHopperResult) route).getPoints();
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
			engine.setCurrentLocation(curr);
			double dist = EQRAgentsHelper.getDistanceFromGPSCood(curr, nxt);
			double st = (dist / sim_speed) + delay * rate;
			try {
				Thread.sleep((long) st);

			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
		engine.setArrived(true);
		engine.setCurrentLocation(points.get(points.size() - 1));
		/*TODO: Notify Base Arrival*/
		
		EQRLogger.log(logger, null, myAgent.getLocalName(), getBehaviourName()
				+ " Fire Engine Arrived at base. Time to rest now");
		MissionCompleteNotificaton mcn = new MissionCompleteNotificaton(MissionCompleteNotificaton.FIREENGINE_MISSION, engine);
		ACLMessage inf = new  ACLMessage(ACLMessage.INFORM);
		inf.addReceiver(command_center);
		try {
			inf.setContentObject(mcn);
			myAgent.send(inf);
			EQRLogger.log(logger, inf, myAgent.getLocalName(), getBehaviourName()
					+ " Mission Complete Notification Sent");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public boolean done() {
		return done;
	}

}
