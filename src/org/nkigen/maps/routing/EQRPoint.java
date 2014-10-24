package org.nkigen.maps.routing;

/**
 * Points on a Map
 * @author nkigen
 *
 */
public class EQRPoint {

	private double longitude;
	private double latitude;
	
	public EQRPoint(double lat, double lon){
		longitude = lon;
		latitude = lat;
	}
	public void setLongitude(double lon){
		this.longitude = lon;
	}
	public void setLatitude(double lat){
		this.latitude = lat;
	}
	public double getLongitude(){
		return longitude;
	}
	public double getLatitude(){
		return latitude;
	}
	
	@Override
	public String toString(){
		return "longitude: "+longitude+ ", latitude "+ latitude;
	}
}
