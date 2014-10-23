package org.nkigen.maps.routing;

/**
 * Specifies the routing criteria for each route
 * @author nkigen
 *
 */
public class EQRRoutingCriteria {

	public static String VEHICLE_CAR = "car";
	protected String locale;
	protected String vehicle;
	protected EQRPoint routeFrom;
	protected EQRPoint routeTo;
	public EQRRoutingCriteria(EQRPoint from, EQRPoint to){
		routeFrom = from;
		routeTo = to;
		vehicle="car";
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
