package org.nkigen.eqr.simulation;

import jade.core.behaviours.SequentialBehaviour;

import org.nkigen.eqr.agents.behaviours.AmbulanceBehaviour;
import org.nkigen.eqr.agents.behaviours.RegisterInDF;
import org.nkigen.eqr.common.EQRAgent;
import org.nkigen.eqr.common.EQRAgentTypes;

public class SimulationAgent extends EQRAgent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected void setup() {
		setType(EQRAgentTypes.SIMULATION_AGENT);
		SequentialBehaviour sb = new SequentialBehaviour();
		sb.addSubBehaviour(new RegisterInDF(this,getMyType(), getMyType()));			
		sb.addSubBehaviour(new SimulationBehaviour(this));
		addBehaviour(sb);	
	}
}
