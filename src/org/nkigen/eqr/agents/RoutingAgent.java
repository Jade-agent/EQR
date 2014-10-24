package org.nkigen.eqr.agents;

import org.nkigen.eqr.agents.behaviours.RoutingBehaviour;

import jade.core.Agent;

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
		if(args != null)
		if(args.length < NUM_ARGS)
			return; /*TODO: to something better!!*/
			
		addBehaviour(new RoutingBehaviour(this,String.valueOf(args[0]),String.valueOf(args[0])));

	}

}
