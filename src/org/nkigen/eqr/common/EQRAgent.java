package org.nkigen.eqr.common;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;

/**
 * Base Agent Class for all the agents in EQR System
 * @author nkigen
 *
 */
public abstract class EQRAgent extends Agent {
	/**
	 * 
	 */
	private String type;
	private static final long serialVersionUID = 1L;
	
	public void setType(String type){
		this.type = type;
	}
	public String getMyType(){
		return type;
	}
	
	
	protected void takeDown(){
		try{
			DFService.deregister(this);
		}
		catch(FIPAException f){
			f.printStackTrace();
		}
	}

}
