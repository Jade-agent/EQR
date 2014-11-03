package org.nkigen.maps.routing.graphhopper;

import java.util.List;

import org.nkigen.eqr.agents.ontologies.routing.EQRRoutingResult;
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
	private PointList points;
	private double distance;
	private long duration;
	private InstructionList turn_instructions;
	private Translation translation;
	private List<String> description;
	private List<GPXEntry> gpx;
	
	
	
	public PointList getPoints() {
		return points;
	}



	public void setPoints(PointList points) {
		this.points = points;
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



	public List<GPXEntry> getGpx() {
		return gpx;
	}



	public void setGpx(List<GPXEntry> gpx) {
		this.gpx = gpx;
	}



	@Override
	public String toString() {
		return "EQRGraphHopperResult [points=" + points + ", distance="
				+ distance + ", duration=" + duration + ", turn_instructions="
				+ turn_instructions + ", translation=" + translation
				+ ", description=" + description + ", gpx=" + gpx + "]";
	}
	
	
	

}
