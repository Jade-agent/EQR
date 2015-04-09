package org.nkigen.eqr.fireengine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jade.util.Logger;

import org.nkigen.eqr.agents.EQRAgentsHelper;
import org.nkigen.eqr.fires.FireDetails;
import org.nkigen.eqr.logs.EQRLogger;
import org.nkigen.eqr.messages.AmbulanceNotifyMessage;
import org.nkigen.eqr.messages.AttendToFireMessage;
import org.nkigen.eqr.messages.EQRRoutingResult;
import org.nkigen.eqr.messages.FireEngineRequestMessage;
import org.nkigen.eqr.messages.PickPatientMessage;
import org.nkigen.eqr.messages.TrafficUpdateMessage;
import org.nkigen.eqr.patients.PatientDetails;
import org.nkigen.maps.routing.EQRPoint;
import org.nkigen.maps.routing.graphhopper.EQRGraphHopperResult;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

/**
 * Plan: - 
 * 
 * @author nkigen
 *
 */
public class AttendToFireBehaviour extends SimpleBehaviour {

	boolean done = false;
	FireDetails fire;
	EQRGraphHopperResult route;
	FireEngineDetails engine;
	Logger logger;
	TrafficUpdateMessage traffic;
	public AttendToFireBehaviour(Agent agent, FireEngineRequestMessage msg,
			FireEngineDetails engine,TrafficUpdateMessage traffic) {
		super(agent);
		this.fire = msg.getFire();
		this.traffic = traffic;
		logger = EQRLogger.prep(logger, myAgent.getLocalName());
		this.route = (EQRGraphHopperResult) msg.getRoute();
		this.engine = engine;
	}

	/*
	 * Only receive message coming from the patientTODO: Also send out status
	 * messages
	 */
	@Override
	public void action() {
		MessageTemplate template = MessageTemplate
				.MatchConversationId(TrafficUpdateMessage.TRAFFIC_SUBSCRIBERS_CONV);
		List<EQRPoint> points = route.getPoints();

		long duration = route.getDuration();
		double distance = route.getDistance();
		double delay = 0;
		double rate = 1; 
		if (traffic != null) {
			delay = traffic.getDelay();
			rate = traffic.getSimulationRate();
		}

		double sim_speed = distance / (duration * rate);
		EQRLogger.log(logger, null, myAgent.getLocalName(),	getBehaviourName()+": Attending to Fire  "+ fire.getAID().getLocalName());
		for (int i = 0; i < points.size() - 1; i++) {

			ACLMessage up = myAgent.receive(template);
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
		
		engine.setCurrentLocation(points.get(points.size() - 1));
		
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		ACLMessage msg2 = new ACLMessage(ACLMessage.INFORM);
		msg2.addReceiver(myAgent.getAID());
		msg.addReceiver(fire.getAID());
		
		AttendToFireMessage atf = new AttendToFireMessage(AttendToFireMessage.TO_FIRE);
		AttendToFireMessage atf2 = new AttendToFireMessage(AttendToFireMessage.TO_FIRE_ENGINE);
		atf.setEngine(engine);
		atf2.setFire(fire);
		try {
			System.out.println(myAgent.getLocalName()
					+ " Sending msgs out to FireEngine and Fire "
					+ fire.getAID());
			msg.setContentObject(atf);
			msg2.setContentObject(atf2);
			EQRLogger.log(logger, msg, myAgent.getLocalName(), getBehaviourName()+": Message sent");
			EQRLogger.log(logger, msg2, myAgent.getLocalName(), getBehaviourName()+": Message sent");
			myAgent.send(msg2);
			myAgent.send(msg);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/* TODO: send msg to patient and */
		done = true;
	}

	@Override
	public boolean done() {
		return done;
	}

}
