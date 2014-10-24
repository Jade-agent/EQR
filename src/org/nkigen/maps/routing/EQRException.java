package org.nkigen.maps.routing;

public class EQRException extends Exception {
	public static String ROUTE_NOT_FOUND = "EQR ERROR: The route could not be found";
	public static String ROUTE_HAS_ERRORS = "EQR ERROR: The route has Errors";
	
	public EQRException(String msg){
		super(msg);
	}

}
