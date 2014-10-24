package org.nkigen.eqr.agents.ontologies.routing;


import org.nkigen.maps.routing.graphhopper.EQRGraphHopperResult;

import jade.content.AgentAction;

public class RoutingOperation implements AgentAction{
	
	private EQRRoutingCriteria criteria;
	private EQRRoutingResult result;
	
	
	@Override
	public String toString() {
		return "RoutingOperation [criteria=" + criteria + ", result=" + result
				+ "]";
	}


	public EQRRoutingCriteria getCriteria() {
		return criteria;
	}


	public void setCriteria(EQRRoutingCriteria criteria) {
		this.criteria = criteria;
	}


	public EQRRoutingResult getResult() {
		return result;
	}


	public void setResult(EQRRoutingResult result) {
		this.result = result;
	}

}
