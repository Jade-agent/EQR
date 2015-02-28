package org.nkigen.eqr.agents;

import jade.core.behaviours.SequentialBehaviour;

import org.nkigen.eqr.agents.behaviours.AmbulanceBehaviour;
import org.nkigen.eqr.agents.behaviours.RegisterInDF;
import org.nkigen.eqr.common.EQRAgent;
import org.nkigen.eqr.common.EQRAgentTypes;

/**
 * Agent for the Emergency Response Team
 * @author nkigen
 *
 */
public class AmbulanceAgent extends EQRAgent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected void setup() {
		setType(EQRAgentTypes.AMBULANCE_AGENT);
		SequentialBehaviour sb = new SequentialBehaviour();
		sb.addSubBehaviour(new RegisterInDF(this,getMyType(), getMyType()));			
		sb.addSubBehaviour(new AmbulanceBehaviour(this));
		addBehaviour(sb);	
	}


}
