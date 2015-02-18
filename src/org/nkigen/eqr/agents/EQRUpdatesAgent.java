package org.nkigen.eqr.agents;

import org.nkigen.eqr.agents.behaviours.UpdateServerBehaviour;
import org.nkigen.eqr.agents.behaviours.ViewerBehaviour;

import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;

public class EQRUpdatesAgent extends Agent {
	

	protected void setup() {
		SequentialBehaviour sb = new SequentialBehaviour();
		sb.addSubBehaviour(new RegisterInDF(this,getMyType(), getMyType()));			
		sb.addSubBehaviour(new UpdateServerBehaviour(this));
		addBehaviour(sb);	
	}

	private String getMyType() {
		return EQRAgentTypes.UPDATES_AGENT;
	}

}
