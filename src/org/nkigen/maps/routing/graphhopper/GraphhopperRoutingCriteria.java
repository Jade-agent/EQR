package org.nkigen.maps.routing.graphhopper;

import org.nkigen.eqr.messages.EQRRoutingCriteria;
import org.nkigen.maps.routing.EQRPoint;

/**
 * Graphhopper specific routing info 
 * https://github.com/graphhopper/graphhopper/blob/master/docs/web/api-doc.md
 * @author nkigen
 *
 */
public class GraphhopperRoutingCriteria extends EQRRoutingCriteria {

	public static String WEIGHTING_FASTEST = "fastest";
	public static String ALGO_DIJKSTRABI = "dijkstrabi";
	public static String TYPE_JSON = "json";
	
	protected boolean instructions = true;
	protected boolean elevation = false; //default false
	protected String weighting;
	protected String algorithm;
	protected boolean points_encoded = false;
	protected boolean debug = true;
	protected boolean calc_points = true;
	protected String type;
	protected int min_path_precision = 1; //dont change
	
	
	public GraphhopperRoutingCriteria(EQRPoint from, EQRPoint to) {
		super(from,to);
		/*Init to default criteria*/
		algorithm = ALGO_DIJKSTRABI;
		weighting = WEIGHTING_FASTEST;
		type = TYPE_JSON; 
	}

	public String getCriteriaAsUrlParams(){
		return "point="+ routeFrom.getLongitude()+"%2C"+routeFrom.getLatitude()
				+"&point="+routeTo.getLongitude()+"%2C"+routeTo.getLongitude()
				+"&type="+this.type;
	}

}
