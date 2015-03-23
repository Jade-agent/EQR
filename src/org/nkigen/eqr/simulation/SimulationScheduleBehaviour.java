package org.nkigen.eqr.simulation;

import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.nkigen.eqr.common.EQRClock;
import org.nkigen.eqr.logs.EQRLogger;
import org.nkigen.eqr.messages.EmergencyScheduleMessage;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

public class SimulationScheduleBehaviour extends OneShotBehaviour {

	int type;
	double mean;
	int num;
	Logger logger;

	/**
	 * @param agent
	 * @param type
	 * @param mean The mean-inter-arrival time in minutes
	 */

	public SimulationScheduleBehaviour(Agent agent, int type, double mean,
			int num) {
		super(agent);
		this.type = type;
		this.mean = mean;
		this.num = num;
		logger = EQRLogger.prep(logger, myAgent.getLocalName());
	}

	@Override
	public void action() {
		EQRClock clock = SimulationBehaviour.getClock();
		RealDistribution rd = new ExponentialDistribution(mean
				* clock.getRate());
		
		double[] s = rd.sample(num);
		for (int i = 0; i < s.length; i++) {
			s[i] = s[i] * clock.getRate();
		}
		EmergencyScheduleMessage esm = new EmergencyScheduleMessage(type);
		esm.setSchedule(s);
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.addReceiver(myAgent.getAID());
		myAgent.send(msg);
		EQRLogger.log(logger, msg, myAgent.getLocalName(),
				"New Simulation schedule created");
	}

}
