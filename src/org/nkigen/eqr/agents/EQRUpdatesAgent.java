package org.nkigen.eqr.agents;

import org.nkigen.eqr.agents.behaviours.RegisterInDF;
import org.nkigen.eqr.agents.behaviours.UpdateServerBehaviour;
import org.nkigen.eqr.agents.behaviours.ViewerBehaviour;
import org.nkigen.eqr.common.EQRAgent;
import org.nkigen.eqr.common.EQRAgentTypes;

import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;

public class EQRUpdatesAgent extends EQRAgent {

	protected void setup() {
		setType(EQRAgentTypes.UPDATES_AGENT);
		SequentialBehaviour sb = new SequentialBehaviour();
		sb.addSubBehaviour(new RegisterInDF(this, getMyType(), getMyType()));
		sb.addSubBehaviour(new UpdateServerBehaviour(this));
		addBehaviour(sb);
	}

}
