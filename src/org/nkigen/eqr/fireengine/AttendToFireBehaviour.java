package org.nkigen.eqr.fireengine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jade.util.Logger;

import org.nkigen.eqr.fires.FireDetails;
import org.nkigen.eqr.logs.EQRLogger;
import org.nkigen.eqr.messages.AmbulanceNotifyMessage;
import org.nkigen.eqr.messages.AttendToFireMessage;
import org.nkigen.eqr.messages.EQRRoutingResult;
import org.nkigen.eqr.messages.FireEngineRequestMessage;
import org.nkigen.eqr.messages.PickPatientMessage;
import org.nkigen.eqr.patients.PatientDetails;
import org.nkigen.maps.routing.EQRPoint;
import org.nkigen.maps.routing.graphhopper.EQRGraphHopperResult;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

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
	public AttendToFireBehaviour(Agent agent, FireEngineRequestMessage msg,
			FireEngineDetails engine) {
		super(agent);
		this.fire = msg.getFire();
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
		List<EQRPoint> points = route.getPoints();

		long duration = route.getDuration();
		double distance = route.getDistance();
		System.out.println(getBehaviourName() + " " + myAgent.getLocalName()
				+ " duration " + duration + " dist " + distance + " points "
				+ route.getPoints().size() + " sleep " + (long) duration
				/ route.getPoints().size());
		EQRLogger.log(logger, null, myAgent.getLocalName(),	getBehaviourName()+": Attending to Fire  "+ fire.getAID().getLocalName());
		for (EQRPoint p : points) {

			engine.setCurrentLocation(p);
			// System.out.println(myAgent.getLocalName()+ " loc: "+ p);

			try {
				Thread.sleep((long) duration / route.getPoints().size());

			} catch (InterruptedException e) {

				e.printStackTrace();
			}
		}
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
