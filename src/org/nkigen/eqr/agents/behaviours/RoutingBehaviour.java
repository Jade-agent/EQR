package org.nkigen.eqr.agents.behaviours;

import org.nkigen.eqr.messages.EQRRoutingCriteria;
import org.nkigen.eqr.messages.EQRRoutingError;
import org.nkigen.eqr.messages.EQRRoutingResult;
import org.nkigen.maps.routing.EQRException;
import org.nkigen.maps.routing.EQRRouter;
import org.nkigen.maps.routing.graphhopper.GraphHopperServer;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

/**
 * Routing behaviour for the agents
 * 
 * @author nkigen
 *
 */
public class RoutingBehaviour extends CyclicBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Agent agent;
	String local_file;
	String storage_dir;
	EQRRouter router;

	public RoutingBehaviour(Agent agent, String local_file, String storage_dir) {
		super(agent);
		this.agent = agent;
		this.local_file = local_file;
		this.storage_dir = storage_dir;
		System.out.println(local_file + " " + storage_dir);
		router = new GraphHopperServer(null, local_file,
				storage_dir);
		
	}

	@Override
	public void action() {
		System.out.println("Router: New message received");
		ACLMessage msg = agent.receive();
		if (msg == null) {
			System.out.println("Router: New message received but its NULL");
			block();
			return;
		}
		try {
			System.out.println("Router: New message received....Message ok Probing");
			Object content = msg.getContentObject();
			switch (msg.getPerformative()) {
			case ACLMessage.REQUEST:
				if (content instanceof EQRRoutingCriteria){
					System.out.println("Router: New message received....Message understood");
					agent.addBehaviour(new HandleSearchRequest(agent, msg));
				}
				else
					replyNotUnderstood(msg);
				break;
			default:
				replyNotUnderstood(msg);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	void replyNotUnderstood(ACLMessage msg) {

		try {
			java.io.Serializable content = msg.getContentObject();
			ACLMessage reply = msg.createReply();
			reply.setPerformative(ACLMessage.NOT_UNDERSTOOD);
			reply.setContentObject(content);
			agent.send(reply);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private class HandleSearchRequest extends OneShotBehaviour {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Agent agent;
		private ACLMessage message;

		public HandleSearchRequest(Agent agent, ACLMessage msg) {
			this.agent = agent;
			this.message = msg;
		}

		@Override
		public void action() {
			try {
				System.out.println("Routing Behaviour: Received Message");
				EQRRoutingCriteria req = (EQRRoutingCriteria) message
						.getContentObject();
				EQRRoutingResult res = null;

				ACLMessage reply = message.createReply();
				try {
					System.out.println("Routing Behaviour: Requesting route from server");
					res = ((GraphHopperServer) router).setCriteria(req).requestRouting()
							.getRoutingResult();
					reply.setPerformative(ACLMessage.INFORM);
				} catch (EQRException e) {
					System.out.println("Routing Behaviour: Error when requesting route...");
					e.getMessage();
					res = new EQRRoutingError();
					reply.setPerformative(ACLMessage.INFORM); /*
															 * TODO: Change this
															 * later
															 */
				} finally {
					System.out.println("Routing Behaviour: Request completed....Sending reply");
					reply.setContentObject(res);
					agent.send(reply);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}