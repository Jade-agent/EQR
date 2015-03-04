package org.nkigen.eqr.agents;

import jade.core.behaviours.SequentialBehaviour;
import jade.core.behaviours.ThreadedBehaviourFactory;

import org.nkigen.eqr.agents.behaviours.EmergencyControlBehaviour;
import org.nkigen.eqr.agents.behaviours.RegisterInDF;
import org.nkigen.eqr.common.EQRAgent;
import org.nkigen.eqr.common.EQRAgentTypes;

/**
 * Emergency Control Center Agent
 * @author nkigen
 *
 */
public class EmergencyControlCenterAgent extends EQRAgent {
	private ThreadedBehaviourFactory tbf;

	protected void setup(){
		tbf =  new ThreadedBehaviourFactory();
		setType(EQRAgentTypes.EMERGENCY_CONTROL_CENTER_AGENT);
		SequentialBehaviour sb = new SequentialBehaviour();
		sb.addSubBehaviour(new RegisterInDF(this,getMyType(), getMyType()));
		sb.addSubBehaviour(tbf.wrap(new EmergencyControlBehaviour(this)));
		addBehaviour(sb);
	}
	public ThreadedBehaviourFactory getTbf(){
		return tbf;
	}
}
