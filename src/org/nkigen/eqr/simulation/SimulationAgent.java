package org.nkigen.eqr.simulation;

import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SequentialBehaviour;
import jade.util.Logger;

import org.nkigen.eqr.agents.behaviours.AmbulanceBehaviour;
import org.nkigen.eqr.agents.behaviours.RegisterInDF;
import org.nkigen.eqr.common.EQRAgent;
import org.nkigen.eqr.common.EQRAgentTypes;
import org.nkigen.eqr.logs.EQRLogger;

/**
 * The Simulation Agent
 * 
 * @author nkigen
 *
 */
public class SimulationAgent extends EQRAgent {

	/**
	 * Number of parameters expected by this agent
	 */
	private static final int NUM_PARAMS = 1;

	protected void setup() {
		String config_file;
		Logger logger = null;
		logger = EQRLogger.prep(logger, getLocalName());
		setType(EQRAgentTypes.SIMULATION_AGENT);

		Object[] params = getArguments();
		if (params.length != NUM_PARAMS) {
			EQRLogger.log(logger, null, getLocalName(),
					"Wrong null of input parameters supplied....exiting");
			return;
		}

		config_file = (String) params[0];
		EQRLogger.log(logger, null, getLocalName(), "Using config file "
				+ config_file);
		SequentialBehaviour sb = new SequentialBehaviour();
		sb.addSubBehaviour(new RegisterInDF(this, getMyType(), getMyType()));
		ParallelBehaviour pb = new ParallelBehaviour();
		pb.addSubBehaviour(new SimulationBehaviour(this, config_file));
		//pb.addSubBehaviour(new TrafficUpdatesBehaviour(this));
		sb.addSubBehaviour(pb);
		addBehaviour(sb);
	}
}
