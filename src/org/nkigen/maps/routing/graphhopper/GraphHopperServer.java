package org.nkigen.maps.routing.graphhopper;

import org.nkigen.maps.routing.EQRRouter;
import org.nkigen.maps.routing.EQRRoutingCriteria;
import org.nkigen.maps.routing.EQRRoutingInterface;

import com.graphhopper.GHRequest;
import com.graphhopper.GraphHopper;
import com.sun.istack.internal.Nullable;

/**
 * Interface to the instance of a graphhopper server
 * @author nkigen
 *
 */
public class GraphHopperServer extends EQRRouter implements EQRRoutingInterface {
	
	private static  String BASE_SERVER_URL = ""; //Set this
	private String currentArea;
	private GraphHopper hopper; 
	private static GraphHopperServer ghs;
	
	protected GraphHopperServer(){ 		
	}
	
	public GraphHopper getGraphHopperInstance(){
		return hopper;
	}
	
	public void setCurrentArea(String area){
		currentArea = area;
	}
	public GraphHopperServer getInstance(){
		if(ghs == null){
			ghs = new GraphHopperServer();
		}
		return ghs;
	}
	
	public void routeRequest(EQRRouter router, EQRRoutingCriteria criteria){
		 
	}
}
