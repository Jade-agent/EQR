package org.nkigen.maps.routing;

import java.io.Serializable;

/**
 * Points on a Map
 * @author nkigen
 *
 */
public class EQRPoint implements Serializable{

	private double longitude;
	private double latitude;
	private double millis;
	
	public double getMillis() {
		return millis;
	}
	public void setMillis(double millis) {
		this.millis = millis;
	}
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
