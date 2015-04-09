package org.nkigen.eqr.simulation.input;

import org.nkigen.maps.routing.EQRPoint;

public class SimulationPoint {
	EQRPoint location;
	
	public SimulationPoint(EQRPoint p) {
		location = p;
	}

	public EQRPoint getLocation() {
		return location;
	}

	public void setLocation(EQRPoint location) {
		this.location = location;
	}
	
	@Override
	public String toString(){
		return location.toString()+"\n";
	}
}
