package org.nkigen.eqr.agents;

import java.io.File;

import org.nkigen.eqr.agents.behaviours.RegisterInDF;
import org.nkigen.eqr.agents.behaviours.RoutingBehaviour;
import org.nkigen.eqr.common.EQRAgentTypes;

import jade.core.Agent;
import jade.core.behaviours.SequentialBehaviour;

/**
 * RoutingAgent 
 * Input arguments: 
 * 		- local map file path
 * 		- Directory to store the files
 * @author nkigen
 *
 */
public class RoutingAgent extends Agent {
	public static int NUM_ARGS = 2;
	
	protected void setup() {
		Object[] args = getArguments();
		//if(args != null)
		//if(args.length < NUM_ARGS)
		//	return; /*TODO: to something better!!*/
		SequentialBehaviour sb = new SequentialBehaviour();
		sb.addSubBehaviour(new RegisterInDF(this,getMyType(), "RA"));
		sb.addSubBehaviour(new RoutingBehaviour(this, "/home/nkigen/development/git/EQR/src/trentino.xml",
				"/home/nkigen/development/git/EQR/src/data"));
		addBehaviour(sb);
	}
	private String getMyType(){
		return EQRAgentTypes.ROUTING_AGENT;
	}

}
