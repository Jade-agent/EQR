package org.nkigen.eqr.agents;

import org.nkigen.eqr.agents.behaviours.AmbulanceBehaviour;
import org.nkigen.eqr.agents.behaviours.FireEngineBehaviour;
import org.nkigen.eqr.agents.behaviours.RoutingBehaviour;

import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;

/**
 * Agent for the Emergency Response Team
 * @author nkigen
 *
 */
public class FireEngineAgent extends Agent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected void setup() {
		SequentialBehaviour sb = new SequentialBehaviour();
		sb.addSubBehaviour(new RegisterInDF(this,getMyType(), getMyType()));
		sb.addSubBehaviour(new FireEngineBehaviour(this));
		addBehaviour(sb);	
	}

	private String getMyType() {
		return EQRAgentTypes.FIRE_ENGINE_AGENT;
	}

}
