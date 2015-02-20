package org.nkigen.eqr.agents;

import jade.core.behaviours.SequentialBehaviour;

import org.nkigen.eqr.agents.behaviours.PatientBehaviour;
import org.nkigen.eqr.agents.behaviours.RegisterInDF;
import org.nkigen.eqr.common.EQRAgent;
import org.nkigen.eqr.common.EQRAgentTypes;

public class EQRPatientAgent extends EQRAgent {

	
	protected void setup(){
		setType(EQRAgentTypes.PATIENT_AGENT);
		SequentialBehaviour sb = new SequentialBehaviour();
		sb.addSubBehaviour(new RegisterInDF(this,getMyType(), getMyType()));
		sb.addSubBehaviour(new PatientBehaviour(this));
		addBehaviour(sb);
	}
}
