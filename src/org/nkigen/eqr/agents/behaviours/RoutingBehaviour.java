package org.nkigen.eqr.agents.behaviours;

import org.nkigen.maps.routing.EQRRouter;
import org.nkigen.maps.routing.graphhopper.GraphHopperServer;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Routing behaviour for the agents
 * 
 * @author nkigen
 *
 */
public class RoutingBehaviour extends CyclicBehaviour {

	private EQRRouter router;
	private static final MessageTemplate msg_template = 
			MessageTemplate.MatchPerformative(ACLMessage.REQUEST);

	public RoutingBehaviour(Agent agent, String local_file, String storage_dir) {
		super(agent);
		router = new GraphHopperServer(null, local_file, storage_dir);
	}

	@Override
	public void action() {

	}

}
