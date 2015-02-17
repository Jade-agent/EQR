package org.nkigen.maps.routing.graphhopper;

import org.nkigen.maps.routing.EQRException;
import org.nkigen.maps.routing.graphhopper.EQRGraphHopperResult;
import org.nkigen.maps.routing.EQRRouter;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.routing.util.EncodingManager;
import com.sun.istack.internal.Nullable;

import org.nkigen.eqr.agents.ontologies.routing.GraphHopperRoutingVocabulary;
import org.nkigen.eqr.messages.EQRRoutingCriteria;
/**
 * Interface to the instance of a graphhopper server
 * 
 * @author nkigen
 *
 */
public class GraphHopperServer extends EQRRouter {

	private GraphHopper hopper;
	private GHRequest req;
	private GHResponse res;
	private String osmFile;
	private String storageDir;
	private EQRRoutingCriteria criteria;

	/**
	 * COnstructor used when the OSM file to be used is from local storage
	 * 
	 * @param criteria
	 * @param osm_file
	 * @param storage_dir
	 */
	public GraphHopperServer(@Nullable EQRRoutingCriteria criteria,
			String osm_file, String storage_dir) {
		super(EQRRouter.ROUTER_GRAPHHOPPER);
		osmFile = osm_file;
		storageDir = storage_dir;
		System.out.println("Graphhopper server called");
		hopper = new GraphHopper().forServer();
		initFromLocal();
		initRoutingCriteria();
		if (criteria != null) {
			this.criteria = criteria;
			initRoutingCriteria();
		}
	}
	
	public GraphHopperServer setCriteria(EQRRoutingCriteria criteria){
		this.criteria = criteria;
		//initRoutingCriteria();
		return this;
	}

	/**
	 * Initialize GraphHopper for an OSM File from local storage
	 */
	private void initFromLocal() {
		hopper.setInMemory(true);
		hopper.setOSMFile(osmFile);
		hopper.setGraphHopperLocation(storageDir);
	}

	/**
	 * Finish initialization of the Routing Criteria
	 */
	private void initRoutingCriteria() {
		System.out.println("Graphhopper Init Routing criteria");
		hopper.setEncodingManager(new EncodingManager(GraphHopperRoutingVocabulary.VEHICLE_CAR));
		hopper.importOrLoad();
	}

	public GraphHopperServer requestRouting() throws EQRException {
System.out.println("Router: New route request received:");
		req = new GHRequest(criteria.getFrom().getLatitude(), criteria
				.getFrom().getLongitude(), criteria.getTo().getLatitude(),
				criteria.getTo().getLongitude()).setWeighting(
				criteria.getWeighting()).setVehicle(criteria.getVehicle());

		res = hopper.route(req);

		if (res.hasErrors()){
			System.out.println("Router: Route has errors");
			throw new EQRException(EQRException.ROUTE_HAS_ERRORS);
		}
		if (!res.isFound()){
			System.out.println("Router: Route not found");
			throw new EQRException(EQRException.ROUTE_NOT_FOUND);
		}

		return this;
	}

	public EQRGraphHopperResult getRoutingResult() {
		EQRGraphHopperResult _response = new EQRGraphHopperResult();

		_response.setPoints(res.getPoints());
		_response.setGpx(res.getInstructions().createGPXList());
		_response.setDistance(res.getDistance());
		_response.setDuration(res.getMillis());

		return _response;
	}

}
