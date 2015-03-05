package org.nkigen.eqr.simulation;

import java.io.IOException;
import java.util.Random;

import org.nkigen.eqr.agents.EQRAgentsHelper;
import org.nkigen.eqr.logs.EQRLogger;
import org.nkigen.eqr.messages.TrafficUpdateMessage;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

public class TrafficUpdatesBehaviour extends TickerBehaviour {

	private static final long UPDATE_PERIOD = 10000;
	private static final long MAX_DELAY = 60000; // Max Delay caused by Traffic
	AID ecc;
	Random rand = new Random();
	Logger logger;
	public TrafficUpdatesBehaviour(Agent agent) {
		super(agent, UPDATE_PERIOD);
		ecc = EQRAgentsHelper.locateControlCenter(myAgent);
		 logger = EQRLogger.prep(logger, myAgent.getLocalName());
	}

	@Override
	protected void onTick() {
		while (ecc == null)
			ecc = EQRAgentsHelper.locateControlCenter(myAgent);
		ACLMessage msg = new ACLMessage(ACLMessage.PROPAGATE);

		TrafficUpdateMessage update = getUpdate();
		msg.addReceiver(ecc);
		try {
			msg.setContentObject(update);
			EQRLogger.log(logger, msg, myAgent.getLocalName(), "Traffic update sent with delay of: "+update.getDelay()+" ms");
			myAgent.send(msg);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private long getRandomDelay() {
		return (long) (rand.nextDouble() * MAX_DELAY);
	}

	private TrafficUpdateMessage getUpdate() {
		TrafficUpdateMessage update = new TrafficUpdateMessage();
		long d = getRandomDelay();
		update.setDelay(d);
		if (d <= 0.75 * MAX_DELAY && d >= MAX_DELAY * 0.25)
			update.setTraffic(TrafficUpdateMessage.TRAFFIC_MEDIUM);
		else if (d > MAX_DELAY * 0.75) {
			update.setTraffic(TrafficUpdateMessage.TRAFFIC_HIGH);
		} else {
			update.setTraffic(TrafficUpdateMessage.TRAFFIC_LOW);
		}
		
		return update;
	}

}
