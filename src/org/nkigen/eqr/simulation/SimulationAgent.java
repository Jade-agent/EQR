package org.nkigen.eqr.simulation;

import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SequentialBehaviour;

import org.nkigen.eqr.agents.behaviours.AmbulanceBehaviour;
import org.nkigen.eqr.agents.behaviours.RegisterInDF;
import org.nkigen.eqr.common.EQRAgent;
import org.nkigen.eqr.common.EQRAgentTypes;

public class SimulationAgent extends EQRAgent {

	/**
	 * Number of parameters expected by this agent
	 */
	private static final int NUM_PARAMS = 1;
	String config_file;// = "/home/nkigen/development/git/EQR/src/config.xml";

	protected void setup() {
		setType(EQRAgentTypes.SIMULATION_AGENT);
		
		Object[] params = getArguments();
		if(params.length != NUM_PARAMS){
			System.err.println("Wrong arguments supplied");
			return;
		}
		config_file = (String) params[0];
		SequentialBehaviour sb = new SequentialBehaviour();
		sb.addSubBehaviour(new RegisterInDF(this, getMyType(), getMyType()));
		ParallelBehaviour pb = new ParallelBehaviour();
		pb.addSubBehaviour(new SimulationBehaviour(this, config_file));
		pb.addSubBehaviour(new TrafficUpdatesBehaviour(this));
		sb.addSubBehaviour(pb);
		addBehaviour(sb);
	}
}
