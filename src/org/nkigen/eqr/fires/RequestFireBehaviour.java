package org.nkigen.eqr.fires;

import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;

public class RequestFireBehaviour extends SimpleBehaviour {

	FireDetails fd;
	boolean done = false;
	
	public RequestFireBehaviour(Agent a, FireDetails fd){
		super(a);
		this.fd = fd;
	}
	
	@Override
	public void action() {
	/*
	 * Request for engine
	 * Start timer and change patient status accordingly
	 * Transfer patient to ambulance	
	 */
	}

	@Override
	public boolean done() {
		return done;
	}

}
