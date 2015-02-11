package org.nkigen.eqr.agents;

import org.nkigen.eqr.agents.behaviours.EmergencyResponseBehaviour;
import org.nkigen.eqr.agents.behaviours.RoutingBehaviour;

import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;

/**
 * Agent for the Emergency Response Team
 * @author nkigen
 *
 */
public class EmergencyResponseAgent extends Agent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected void setup() {
		SequentialBehaviour sb = new SequentialBehaviour();
		sb.addSubBehaviour(new RegisterInDF(this,getMyType(), "ERA"));			
		sb.addSubBehaviour(new EmergencyResponseBehaviour(this));
		addBehaviour(sb);	
	}

	private String getMyType() {
		return EQRAgentTypes.EMERGENCY_RESPONSE_AGENT;
	}

}
