package org.nkigen.maps.routing.graphhopper;



import java.util.ArrayList;
import java.util.List;

import org.nkigen.eqr.agents.ontologies.routing.EQRRoutingResult;
import org.nkigen.maps.routing.EQRPoint;
import org.nkigen.maps.routing.graphhopper.GraphHopperServer;

import com.graphhopper.util.GPXEntry;
import com.graphhopper.util.InstructionList;
import com.graphhopper.util.PointList;
import com.graphhopper.util.Translation;

/**
 * This packages all the Route Results from {@link GraphHopperServer} results
 * @author nkigen
 *
 */
public class EQRGraphHopperResult extends EQRRoutingResult {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7177713925733169887L;
	/**
	 * For now only the PointList, distance, duration gpx are used
	 */
	private ArrayList<EQRPoint> points;
	private double distance;
	private long duration;
	private  InstructionList turn_instructions;
	private  Translation translation;
	private List<String> description;
	private List<EQRPoint> gpx;
	
	
	
	public List<EQRPoint> getPoints() {
		
		return points;
	}

	public void setPoints(PointList points) {
		if(this.points == null)
			this.points = new ArrayList<EQRPoint>();
		for(int i=0; i<points.size();++i){
			EQRPoint p = new EQRPoint(points.getLat(i), points.getLon(i));
			this.points.add(p);
		} 
	}



	public double getDistance() {
		return distance;
	}



	public void setDistance(double distance) {
		this.distance = distance;
	}



	public long getDuration() {
		return duration;
	}



	public void setDuration(long duration) {
		this.duration = duration;
	}



	public InstructionList getTurn_instructions() {
		return turn_instructions;
	}



	public void setTurn_instructions(InstructionList turn_instructions) {
		this.turn_instructions = turn_instructions;
	}



	public Translation getTranslation() {
		return translation;
	}



	public void setTranslation(Translation translation) {
		this.translation = translation;
	}



	public List<String> getDescription() {
		return description;
	}



	public void setDescription(List<String> description) {
		this.description = description;
	}



	public List<EQRPoint> getGpx() {
		return gpx;
	}



	public void setGpx(List<GPXEntry> gpx) {
		if(this.gpx == null)
			this.gpx = new ArrayList<EQRPoint>();
		for(int i=0; i<gpx.size();++i){
			EQRPoint p = new EQRPoint(gpx.get(i).getLat(), gpx.get(i).getLon());
			p.setMillis(gpx.get(i).getMillis());
			this.gpx.add(p);
		}
	}



	@Override
	public String toString() {
		return "EQRGraphHopperResult [points=" + points + ", distance="
				+ distance + ", duration=" + duration + ", turn_instructions="
				+ turn_instructions + ", translation=" + translation
				+ ", description=" + description + ", gpx=" + gpx + "]";
	}
	
	
	

}
