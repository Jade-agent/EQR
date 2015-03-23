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
	AID ecc;
	Random rand = new Random();
	Logger logger;
	long update_period;
	long max_delay;

	public TrafficUpdatesBehaviour(Agent agent, long update_period,
			long max_delay) {
		super(agent, update_period);
		this.update_period = update_period;
		this.max_delay = max_delay;
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
			myAgent.send(msg);
			EQRLogger.log(logger, msg, myAgent.getLocalName(),
					"Current Traffic Delay: " + update.getDelay() + " ms");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private long getRandomDelay() {
		return (long) (rand.nextDouble() * max_delay);
	}

	private TrafficUpdateMessage getUpdate() {
		TrafficUpdateMessage update = new TrafficUpdateMessage();
		long d = getRandomDelay();
		update.setDelay(d);
		if (d <= 0.75 * max_delay && d >= max_delay * 0.25)
			update.setTraffic(TrafficUpdateMessage.TRAFFIC_MEDIUM);
		else if (d > max_delay * 0.75) {
			update.setTraffic(TrafficUpdateMessage.TRAFFIC_HIGH);
		} else {
			update.setTraffic(TrafficUpdateMessage.TRAFFIC_LOW);
		}

		return update;
	}

}
