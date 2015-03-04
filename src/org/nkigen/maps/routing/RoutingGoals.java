package org.nkigen.maps.routing;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import org.nkigen.eqr.agents.RoutingAgent;
import org.nkigen.eqr.common.EQRGoal;
import org.nkigen.eqr.messages.MultipleRoutingRequestMessage;
import org.nkigen.maps.routing.graphhopper.GraphHopperServer;

public class RoutingGoals extends EQRGoal {

	public static final int FIND_ROUTE_FROM_SINGLE = 0;
	public static final int FIND_ROUTE_FROM_MULTIPLE = 1;

	@Override
	public Behaviour executePlan(int which, Object[] params) {

		switch (which) {
		case FIND_ROUTE_FROM_SINGLE:
			return findRouteFromSingle(params);
		case FIND_ROUTE_FROM_MULTIPLE:
			return findRouteFromMultiple(params);
		}
		return null;
	}

	private Behaviour findRouteFromSingle(Object[] p) {
		if (p.length != 3)
			return null;
		if (p[0] instanceof RoutingAgent && p[1] instanceof ACLMessage
				&& p[2] instanceof GraphHopperServer)
			return new FindSingleRouteBehaviour((RoutingAgent) p[0],
					(ACLMessage) p[1], (GraphHopperServer) p[2]);
		return null;
	}

	private Behaviour findRouteFromMultiple(Object[] p) {
		if (p.length != 3)
			return null;
		if (p[0] instanceof RoutingAgent && p[1] instanceof MultipleRoutingRequestMessage)
			return new FindMultipleRouteBehaviour((RoutingAgent) p[0],
					(MultipleRoutingRequestMessage) p[1],(String)p[2]);
		return null;
	}

	@Override
	public void newGoal(int which, Class behaviour) {
		// TODO Auto-generated method stub

	}

}
