package org.nkigen.eqr.agents.behaviours;

import org.nkigen.eqr.messages.EQRRoutingCriteria;
import org.nkigen.eqr.messages.EQRRoutingError;
import org.nkigen.eqr.messages.EQRRoutingResult;
import org.nkigen.eqr.messages.MultipleRoutingRequestMessage;
import org.nkigen.maps.routing.EQRException;
import org.nkigen.maps.routing.EQRRouter;
import org.nkigen.maps.routing.RoutingGoals;
import org.nkigen.maps.routing.graphhopper.GraphHopperServer;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
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
	RoutingGoals goals;

	public RoutingBehaviour(Agent agent, String local_file, String storage_dir) {
		super(agent);
		this.agent = agent;
		this.local_file = local_file;
		this.storage_dir = storage_dir;
		System.out.println(local_file + " " + storage_dir);
		router = new GraphHopperServer(null, local_file, storage_dir);
		goals = new RoutingGoals();

	}

	@Override
	public void action() {
		ACLMessage msg = agent.receive();
		if (msg == null) {
			block();
			return;
		}
		try {
			System.out
					.println("Router: New message received....Message ok Probing");
			Object content = msg.getContentObject();
			switch (msg.getPerformative()) {
			case ACLMessage.REQUEST:
				if (content instanceof EQRRoutingCriteria) {
					System.out.println(getBehaviourName()
							+ " New Single Request received");
					Object[] params = new Object[3];
					params[0] = myAgent;
					params[1] = msg;
					params[2] = router;
					Behaviour b = goals.executePlan(
							RoutingGoals.FIND_ROUTE_FROM_SINGLE, params);
					agent.addBehaviour(b);
				} else if (content instanceof MultipleRoutingRequestMessage) {
					System.out.println(getBehaviourName()
							+ " New Multiple Route Request received");
					Object[] params = new Object[2];
					params[0] = myAgent;
					((MultipleRoutingRequestMessage) content).setReply_to(msg
							.getSender());
					((MultipleRoutingRequestMessage) content)
							.setRouter((GraphHopperServer) router);
					params[1] = (MultipleRoutingRequestMessage) content;
					Behaviour b = goals.executePlan(
							RoutingGoals.FIND_ROUTE_FROM_MULTIPLE, params);
					agent.addBehaviour(b);
				} else
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

}