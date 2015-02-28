package org.nkigen.eqr.agents;

import jade.core.behaviours.SequentialBehaviour;

import org.nkigen.eqr.agents.behaviours.FireBehaviour;
import org.nkigen.eqr.agents.behaviours.PatientBehaviour;
import org.nkigen.eqr.agents.behaviours.RegisterInDF;
import org.nkigen.eqr.common.EQRAgent;
import org.nkigen.eqr.common.EQRAgentTypes;

public class EQRFireAgent extends EQRAgent {

	
	protected void setup(){
		setType(EQRAgentTypes.FIRE_AGENT);
		SequentialBehaviour sb = new SequentialBehaviour();
		sb.addSubBehaviour(new RegisterInDF(this,getMyType(), getMyType()));
		sb.addSubBehaviour(new FireBehaviour(this));
		addBehaviour(sb);
	}
}
