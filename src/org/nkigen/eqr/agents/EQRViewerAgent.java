package org.nkigen.eqr.agents;

import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;

import org.nkigen.eqr.agents.behaviours.EmergencyResponseBehaviour;
import org.nkigen.eqr.agents.behaviours.ViewerBehaviour;

/**
 * Agent for the Emergency Response Team
 * @author nkigen
 *
 */
public class EQRViewerAgent extends Agent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected void setup() {
		SequentialBehaviour sb = new SequentialBehaviour();
		sb.addSubBehaviour(new RegisterInDF(this,getMyType(), "VIEWER_AGENT"));			
		sb.addSubBehaviour(new ViewerBehaviour(this));
		addBehaviour(sb);	
	}

	private String getMyType() {
		return EQRAgentTypes.VIEWER_AGENT;
	}

}

