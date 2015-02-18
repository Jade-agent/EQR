package org.nkigen.eqr.agents.behaviours;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.nkigen.eqr.agents.EQRAgentsHelper;
import org.nkigen.eqr.messages.EQRLocationUpdate;
import org.nkigen.eqr.messages.EQRRoutingResult;
import org.nkigen.eqr.models.EQREmergencyPoint;
import org.nkigen.maps.routing.EQRPoint;
import org.nkigen.maps.routing.graphhopper.EQRGraphHopperResult;
import org.nkigen.maps.viewer.updates.EQRAmbulanceLocations;
import org.nkigen.maps.viewer.updates.EQRFireEngineLocation;
import org.nkigen.maps.viewer.updates.EQRFiresUpdatesItem;
import org.nkigen.maps.viewer.updates.EQRPatientStatusItem;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;

public class FireManagerBehaviour extends SimpleBehaviour {

	boolean done;
	EQRRoutingResult route;
	EQREmergencyPoint engine;
	EQREmergencyPoint fire;
	AID update_server;

	List<EQRPoint> route_to_fire;
	long duration;
	int next_index;

	public FireManagerBehaviour(Agent agent, EQRRoutingResult route,
			EQREmergencyPoint engine, EQREmergencyPoint fire) {
		super(agent);
		this.engine = engine;
		this.fire = fire;
		this.route = route;
		if (route instanceof EQRGraphHopperResult) {
			route_to_fire = ((EQRGraphHopperResult) route).getPoints();
			duration = ((EQRGraphHopperResult) route).getDuration();
		}
		update_server = EQRAgentsHelper.locateUpdateServer(myAgent);
	}

	@Override
	public void action() {

		/* TODO: Add traffic data */
		//System.out.println("FMB: " + next_index + " size "
			//	+ route_to_fire.size());
		if (next_index < route_to_fire.size()) {
			EQRPoint point = route_to_fire.get(next_index);

			EQRLocationUpdate fire_loc1 = new EQRLocationUpdate(
					EQRLocationUpdate.FIRE_ENGINE_LOCATION, engine.getId());
			EQRLocationUpdate loc1 = new EQRLocationUpdate(
					EQRLocationUpdate.FIRE_LOCATION, fire.getId());

			fire_loc1.setIsMoving(true);
			fire_loc1.setIsDead(false);
			fire_loc1.setItemId(engine.getId());
			fire_loc1.setCurrent(point);
			fire_loc1.setHeading(new EQRPoint(fire.getLatitude(), fire
					.getLongitude()));
			fire_loc1.setIsMoving(true);

			if (next_index == 0) {
				loc1.setIsMoving(false);
				loc1.setIsDead(false);
				loc1.setCurrent(new EQRPoint(fire.getLatitude(), fire
						.getLongitude()));
				loc1.setIsMoving(false);
				loc1.setItemId(fire.getId());
			}

			if (update_server == null) {
				update_server = EQRAgentsHelper.locateUpdateServer(myAgent);
			}

			ACLMessage msg1 = new ACLMessage(ACLMessage.PROPAGATE);
			ACLMessage msg2 = new ACLMessage(ACLMessage.PROPAGATE);
			try {
				msg1.setContentObject(fire_loc1);
				msg2.setContentObject(loc1);
				msg1.addReceiver(update_server);
				msg2.addReceiver(update_server);
				myAgent.send(msg1);
				myAgent.send(msg2);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			next_index++;
			block(duration / route_to_fire.size());
		}

	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
