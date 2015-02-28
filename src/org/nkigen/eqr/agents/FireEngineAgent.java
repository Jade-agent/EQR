package org.nkigen.eqr.agents;

import org.nkigen.eqr.agents.behaviours.AmbulanceBehaviour;
import org.nkigen.eqr.agents.behaviours.FireEngineBehaviour;
import org.nkigen.eqr.agents.behaviours.RegisterInDF;
import org.nkigen.eqr.agents.behaviours.RoutingBehaviour;
import org.nkigen.eqr.common.EQRAgent;
import org.nkigen.eqr.common.EQRAgentTypes;

import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;

/**
 * Agent for the Emergency Response Team
 * @author nkigen
 *
 */
public class FireEngineAgent extends EQRAgent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected void setup() {
		setType(EQRAgentTypes.FIRE_ENGINE_AGENT);
		SequentialBehaviour sb = new SequentialBehaviour();
		sb.addSubBehaviour(new RegisterInDF(this,getMyType(), getMyType()));
		sb.addSubBehaviour(new FireEngineBehaviour(this));
		addBehaviour(sb);	
	}

}
