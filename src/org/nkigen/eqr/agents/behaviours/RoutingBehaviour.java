package org.nkigen.eqr.agents.behaviours;

import org.nkigen.eqr.agents.ontologies.routing.EQRRoutingCriteria;
import org.nkigen.eqr.agents.ontologies.routing.EQRRoutingError;
import org.nkigen.eqr.agents.ontologies.routing.EQRRoutingResult;
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

	public RoutingBehaviour(Agent agent, String local_file, String storage_dir) {
		super(agent);
		this.agent = agent;
		this.local_file = local_file;
		this.storage_dir = storage_dir;
	}

	@Override
	public void action() {
		ACLMessage msg = agent.receive();
		if (msg == null) {
			block();
			return;
		}
		try {
			Object content = msg.getContentObject();
			switch (msg.getPerformative()) {
			case ACLMessage.REQUEST:
				if (content instanceof EQRRoutingCriteria)
					agent.addBehaviour(new HandleSearchRequest(agent, msg));
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
				EQRRoutingCriteria req = (EQRRoutingCriteria) message
						.getContentObject();
				EQRRouter router = new GraphHopperServer(req, local_file,
						storage_dir);
				EQRRoutingResult res = null;

				ACLMessage reply = message.createReply();
				try {
					res = ((GraphHopperServer) router).requestRouting()
							.getRoutingResult();
					reply.setPerformative(ACLMessage.INFORM);
				} catch (EQRException e) {
					res = new EQRRoutingError();
					reply.setPerformative(ACLMessage.INFORM); /*
															 * TODO: Change this
															 * later
															 */
				} finally {
					reply.setContentObject(res);
					agent.send(reply);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}