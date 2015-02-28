package org.nkigen.maps.routing;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import org.nkigen.eqr.agents.EQRAgentsHelper;
import org.nkigen.eqr.messages.EQRRoutingCriteria;
import org.nkigen.eqr.messages.EQRRoutingError;
import org.nkigen.eqr.messages.EQRRoutingResult;
import org.nkigen.maps.routing.graphhopper.GraphHopperServer;

public class FindSingleRouteBehaviour extends OneShotBehaviour {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ACLMessage message;
	GraphHopperServer router;

	public FindSingleRouteBehaviour(Agent agent, ACLMessage msg,
			GraphHopperServer router) {
		super(agent);
		this.message = msg;
		this.router = router;
	}

	@Override
	public void action() {

		try {
			System.out.println(getBehaviourName() + " msg received");
			EQRRoutingCriteria req = (EQRRoutingCriteria) message
					.getContentObject();
			EQRRoutingResult res = null;

			ACLMessage reply = message.createReply();
			try {
				System.out
						.println(getBehaviourName()+ " Requesting route from server");
				res = ((GraphHopperServer) router).setCriteria(req)
						.requestRouting().getRoutingResult();
				reply.setPerformative(ACLMessage.INFORM);
			} catch (EQRException e) {
				System.out
						.println(getBehaviourName()+ "Error when requesting route...");
				e.getMessage();
				res = new EQRRoutingError();
				reply.setPerformative(ACLMessage.INFORM); /*
														 * TODO: Change this
														 * later
														 */
			} finally {
				System.out
						.println("Routing Behaviour: Request completed....Sending reply");
				reply.setContentObject(res);
				myAgent.send(reply);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
