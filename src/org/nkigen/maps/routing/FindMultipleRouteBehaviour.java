package org.nkigen.maps.routing;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.ArrayList;

import org.nkigen.eqr.common.EmergencyResponseBase;
import org.nkigen.eqr.messages.EQRRoutingCriteria;
import org.nkigen.eqr.messages.EQRRoutingError;
import org.nkigen.eqr.messages.EQRRoutingResult;
import org.nkigen.eqr.messages.MultipleRoutingRequestMessage;
import org.nkigen.eqr.messages.MultipleRoutingResponseMessage;
import org.nkigen.maps.routing.graphhopper.EQRGraphHopperResult;
import org.nkigen.maps.routing.graphhopper.GraphHopperServer;

public class FindMultipleRouteBehaviour extends OneShotBehaviour {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	MultipleRoutingRequestMessage request;
	String conversation_id;
	public FindMultipleRouteBehaviour(Agent agent,
			MultipleRoutingRequestMessage request,String con_id) {
		super(agent);
		conversation_id = con_id;
		this.request = request;
	}

	@Override
	public void action() {
		System.out.println(getBehaviourName() + " msg received");

		EQRRoutingCriteria req = null;
		ACLMessage reply = new ACLMessage(ACLMessage.INFORM);
		reply.addReceiver(request.getReply_to());
		reply.setConversationId(conversation_id);
		EQRRoutingResult res = null;
		EmergencyResponseBase closest = null;
		EQRGraphHopperResult closest_res = null;

		ArrayList<EmergencyResponseBase> bases = request.getBases();
		if (bases != null) {
			System.out.println(getBehaviourName()+ " Finding shortest route from " +bases.size() +" possibilities");
			for (EmergencyResponseBase b : request.getBases()) {
				EQRPoint from = b.getLocation();
				req = new EQRRoutingCriteria(from, request.getTo());
				try {
					System.out.println(getBehaviourName()
							+ " Requesting route from server");
					res = ((GraphHopperServer) request.getRouter())
							.setCriteria(req).requestRouting()
							.getRoutingResult();
					if (closest == null && closest_res == null
							&& res instanceof EQRGraphHopperResult) {
						closest = b;
						closest_res = (EQRGraphHopperResult) res;
					} else if (((EQRGraphHopperResult) res).getDistance() < closest_res
							.getDistance()) {
						closest = b;
						closest_res = (EQRGraphHopperResult) res;
					}
				} catch (EQRException e) {
					System.out.println(getBehaviourName()
							+ "Error when requesting route...");
					e.getMessage();

				}
			}
			System.out.println("Route found to "+ req.getTo()+" FROM "+ closest.getLocation() );
		}

		reply.setPerformative(ACLMessage.INFORM);
		if (closest == null && closest_res == null) {
			res = new EQRRoutingError();
		}

		MultipleRoutingResponseMessage response = new MultipleRoutingResponseMessage();
		response.setBase(closest);
		response.setResult(res);
		try {
			reply.setContentObject(response);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		myAgent.send(reply);
		System.out.println(getBehaviourName()
				+ " Request completed....Sending reply");
	}

}
