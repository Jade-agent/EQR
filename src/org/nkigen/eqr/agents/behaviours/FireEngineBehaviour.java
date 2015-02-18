package org.nkigen.eqr.agents.behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import org.nkigen.eqr.agents.EQRAgentsHelper;
import org.nkigen.eqr.messages.EQRRoutingCriteria;
import org.nkigen.eqr.messages.EQRRoutingError;
import org.nkigen.eqr.messages.EQRRoutingResult;
import org.nkigen.eqr.models.EQREmergencyPoint;
import org.nkigen.eqr.models.EmergencyHandler;
import org.nkigen.maps.routing.EQRPoint;
import org.nkigen.maps.routing.graphhopper.EQRGraphHopperResult;

public class FireEngineBehaviour extends CyclicBehaviour {

	Agent agent;
	private AID routing_server;
	private AID viewer;
	private AID update_server;
	EQREmergencyPoint to_serve;

	public FireEngineBehaviour(Agent agent) {
		super(agent);
		this.agent = agent;
		// startSimulation();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		
		if (isNewEmergency()) {
			agent.addBehaviour(new NewRequestBehaviour(to_serve));
		} else
			block();
	}

	private boolean isNewEmergency() {
		to_serve = EmergencyHandler.getInstance().getFirePoint();
		if (to_serve == null)
			return false;
		System.out.println(":New Fire Found at "+ to_serve.getLatitude() + ", " +to_serve.getLongitude());
		return true;
	}

	private class NewRequestBehaviour extends SimpleBehaviour {

		EQREmergencyPoint loc;
		boolean done = false;

		public NewRequestBehaviour(EQREmergencyPoint loc) {
			this.loc = loc;
		}

		@Override
		public void action() {
			EQREmergencyPoint p = EmergencyHandler.getInstance().getFireEngine(
					true);
			if (p != null) {
				EQRRoutingCriteria c = new EQRRoutingCriteria(new EQRPoint(
						p.getLatitude(), p.getLongitude()), new EQRPoint(
						loc.getLatitude(), loc.getLongitude()));
				sendMessage(ACLMessage.REQUEST, c, p, loc);
				done = true;
			}
		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return done;
		}

	}

	void sendMessage(int performative, Object content, EQREmergencyPoint engine, EQREmergencyPoint fire) {
		
		routing_server = EQRAgentsHelper.locateRoutingServer(agent);	
		ACLMessage msg = new ACLMessage(performative);
		try {
			msg.setContentObject((java.io.Serializable) content);
			msg.addReceiver(routing_server);
			System.out.println("Contacting server... Please wait!");
			agent.send(msg);
			agent.addBehaviour(new WaitRouterResponse(engine, fire));
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private class WaitRouterResponse extends ParallelBehaviour {
		public WaitRouterResponse(EQREmergencyPoint engine, EQREmergencyPoint fire) {
			super(agent, 1);
			addSubBehaviour(new ReceiveResponse(engine, fire));
			addSubBehaviour(new WakerBehaviour(agent, 5000) {

				protected void handleElapsedTimeout() {
					System.out
							.println("\n\tNo response from server. Please, try later!");
					agent.addBehaviour(FireEngineBehaviour.this);
				}
			});
		}

	}

	private class ReceiveResponse extends SimpleBehaviour {

		boolean finished = false;
		EQREmergencyPoint engine;
		EQREmergencyPoint fire;
		public ReceiveResponse(EQREmergencyPoint engine, EQREmergencyPoint fire) {
			this.fire = fire;
			this.engine = engine;
		}

		@Override
		public void action() {
			System.out.println("Emergency Response: Result received");
			ACLMessage msg = agent.receive();
			if (msg == null) {
				System.out
						.println("Emergency Receiver: New message received but its NULL");
				block();
				return;
			}
			try {
				System.out
						.println("Emergency Receiver: New message received....Message ok Probing");
				Object content = msg.getContentObject();
				switch (msg.getPerformative()) {
				case ACLMessage.INFORM:
					if (content instanceof EQRRoutingResult) {
						System.out
								.println("Emergency Recv: New message received....Message understood");
						handle(msg);
						finished = true;
						return;
					} else {
						System.out
								.println("Emergency Recv: Reply from router not understood");
						finished = true;
					}
					break;
				default:
					System.out.println("Emergency Recv: Wrong msg from router");
					finished = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		private void handle(ACLMessage msg) {
			System.out.println("Emergency Recv: Handling reply from server");
			try {
				EQRRoutingResult result = (EQRRoutingResult) msg
						.getContentObject();
				if (result instanceof EQRRoutingError) {
					System.out
							.println("Emergency Recv: Requested Route has errors");
					/*
					 * No Route to Place
					 */
				} else if (result instanceof EQRGraphHopperResult) {
					agent.addBehaviour(new FireManagerBehaviour(agent, result, engine, fire));

				}
				else{
					agent.postMessage(msg);
				}
			} catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public boolean done() {
			return finished;
		}

	}

}
