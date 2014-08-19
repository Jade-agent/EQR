package org.nkigen.maps.routing;

/**
 * Specifies the routing criteria for each route
 * @author nkigen
 *
 */
public class EQRRoutingCriteria {

	public static String VEHICLE_CAR = "car";
	private String longitude;
	private String latitude;
	private String locale;
	private String vehicle;
	public EQRRoutingCriteria(String lon, String lat){
		longitude = lon;
		latitude = lat;
	}
	
}
