package org.nkigen.maps.routing;

/**
 * Points on a Map
 * @author nkigen
 *
 */
public class EQRPoint {

	private String longitude;
	private String latitude;
	
	public EQRPoint(String lat, String lon){
		longitude = lon;
		latitude = lat;
	}
	public void setLongitude(String lon){
		this.longitude = lon;
	}
	public void setLatitude(String lat){
		this.latitude = lat;
	}
	public String getLongitude(){
		return longitude;
	}
	public String getLatitude(){
		return latitude;
	}
	
	@Override
	public String toString(){
		return "longitude: "+longitude+ ", latitude "+ latitude;
	}
}
