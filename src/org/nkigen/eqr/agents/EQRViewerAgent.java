package org.nkigen.eqr.agents;

import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;

import org.apache.commons.collections.set.CompositeSet.SetMutator;
import org.nkigen.eqr.agents.behaviours.AmbulanceBehaviour;
import org.nkigen.eqr.agents.behaviours.RegisterInDF;
import org.nkigen.eqr.agents.behaviours.ViewerBehaviour;
import org.nkigen.eqr.common.EQRAgent;
import org.nkigen.eqr.common.EQRAgentTypes;

/**
 * Agent for the Emergency Response Team
 * @author nkigen
 *
 */
public class EQRViewerAgent extends EQRAgent{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected void setup() {
		setType(EQRAgentTypes.VIEWER_AGENT);
		SequentialBehaviour sb = new SequentialBehaviour();
		sb.addSubBehaviour(new RegisterInDF(this,getMyType(), getMyType()));			
		sb.addSubBehaviour(new ViewerBehaviour(this));
		addBehaviour(sb);	
	}

	

}

