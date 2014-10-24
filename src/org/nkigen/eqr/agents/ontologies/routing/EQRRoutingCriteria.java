package org.nkigen.eqr.agents.ontologies.routing;

import org.nkigen.maps.routing.EQRPoint;

import jade.content.Concept;

/**
 * Specifies the routing criteria for each route
 * @author nkigen
 *
 */
public class EQRRoutingCriteria implements Concept, GraphHopperRoutingVocabulary {
	protected String locale;
	protected String vehicle;
	protected EQRPoint routeFrom;
	protected EQRPoint routeTo;
	protected String weighting;
	public EQRRoutingCriteria(EQRPoint from, EQRPoint to){
		routeFrom = from;
		routeTo = to;
		vehicle = VEHICLE_CAR;
		weighting = WEIGHTING_FASTEST;
		
	}
	public String getWeighting(){
		return weighting;
	}
	public String getVehicle(){
		return vehicle;
	}
	public EQRPoint getFrom(){
		return routeFrom;
	}
	public EQRPoint getTo(){
		return routeTo;
	}
	
}
