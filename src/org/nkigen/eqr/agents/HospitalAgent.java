package org.nkigen.eqr.agents;

import jade.core.behaviours.SequentialBehaviour;

import org.nkigen.eqr.agents.behaviours.HospitalBehaviour;
import org.nkigen.eqr.agents.behaviours.RegisterInDF;
import org.nkigen.eqr.common.EQRAgent;
import org.nkigen.eqr.common.EQRAgentTypes;

public class HospitalAgent extends EQRAgent{

	
	protected void setup(){
		setType(EQRAgentTypes.HOSPITAL_AGENT);
		SequentialBehaviour sb = new SequentialBehaviour();
		sb.addSubBehaviour(new RegisterInDF(this,getMyType(), getMyType()));
		sb.addSubBehaviour(new HospitalBehaviour(this));
		addBehaviour(sb);
	}
}
