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
	public EngineBackToBase(Agent agent, FireEngineDetails engine) {
		super(agent);
		this.engine = engine;
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
		System.out.println(myAgent.getLocalName()
				+ " : Finally I'm heading to Base. CurrentLocation is "
				+ engine.getCurrentLocation());
		EQRLogger.log(logger, null, myAgent.getLocalName(), getBehaviourName()
				+ ":Received route to Base");
		EQRRoutingResult route = msg.getResult();
		if (route instanceof EQRRoutingError) {
			System.out.println(myAgent.getLocalName()
					+ " Route to base wasn't found!!");
			EQRLogger.log(EQRLogger.LOG_EERROR, logger, null,
					myAgent.getLocalName(), getBehaviourName()
							+ ": No route to Base found!!");
			return;
		}

		List<EQRPoint> points = ((EQRGraphHopperResult) route).getPoints();
		long duration = ((EQRGraphHopperResult) route).getDuration();
		EQRLogger.log(logger, null, myAgent.getLocalName(),
				getBehaviourName() + ": Route of duration: " + duration
						+ " and points :" + points.size() + " found ");
		for (EQRPoint p : points) {

			engine.setCurrentLocation(p);
			// System.out.println(myAgent.getLocalName()+ " loc: "+ p);

			try {
				Thread.sleep((long) duration / points.size());

			} catch (InterruptedException e) {

				e.printStackTrace();
			}

		}
		EQRLogger.log(logger, null, myAgent.getLocalName(), getBehaviourName()
				+ " Fire Engine Arrived at base. Time to rest now");
	}

	@Override
	public boolean done() {
		return done;
	}

}
