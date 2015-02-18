package org.nkigen.eqr.agents.behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import org.nkigen.eqr.agents.EQRAgentsHelper;
import org.nkigen.eqr.messages.EQRRoutingCriteria;
import org.nkigen.eqr.messages.EQRRoutingError;
import org.nkigen.eqr.messages.EQRRoutingResult;
import org.nkigen.eqr.models.EmergencyHandler;
import org.nkigen.maps.routing.graphhopper.EQRGraphHopperResult;

public class FireEngineBehaviour extends CyclicBehaviour {

	Agent agent;
	private AID routing_server;
	private AID update_server;
	
	EQRRoutingCriteria to_serve;
	public FireEngineBehaviour(Agent agent) {
		super(agent);
		this.agent = agent;
	//	startSimulation();
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		if (isNewEmergency()) {
			EQRRoutingCriteria loc = getLocation();
			agent.addBehaviour(new NewRequestBehaviour(loc));
		} else
			block();

	}

	private boolean isNewEmergency() {
		
		to_serve = EmergencyHandler.getInstance().getRoute();
		if(to_serve == null)
			return false;
		System.out.println("New emergency to be generated");
		block(1000);
		return true;
	}

	private EQRRoutingCriteria getLocation() {
		EQRRoutingCriteria loc = to_serve;//new EQRRoutingCriteria(new EQRPoint(46.071944, 11.119444), new EQRPoint(46.056332,11.133385));
		to_serve = null;
		return loc;
	}

	private class NewRequestBehaviour extends OneShotBehaviour {

		EQRRoutingCriteria loc;

		public NewRequestBehaviour(EQRRoutingCriteria loc) {
			this.loc = loc;
		}

		@Override
		public void action() {
			sendMessage(ACLMessage.REQUEST, loc);
		}

	}

	void sendMessage(int performative, Object content) {
		// ----------------------------------------------------

		if (routing_server == null){
			routing_server = EQRAgentsHelper.locateRoutingServer(agent);
			System.out.println("Routing server located!!");
		}
		if (routing_server == null) {
			System.out
					.println("Unable to localize the server! Operation aborted!");
			return;
		}
		ACLMessage msg = new ACLMessage(performative);
		try {
			msg.setContentObject((java.io.Serializable) content);
			msg.addReceiver(routing_server);
			System.out.println("Contacting server... Please wait!");
			agent.send(msg);
			agent.addBehaviour(new WaitRouterResponse());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}


		
	
	private class WaitRouterResponse extends ParallelBehaviour {
		public WaitRouterResponse(){
		     super(agent, 1);
		     addSubBehaviour(new ReceiveResponse());
		     addSubBehaviour(new WakerBehaviour(agent, 5000) {

			    protected void handleElapsedTimeout() {
				   System.out.println("\n\tNo response from server. Please, try later!");
				   agent.addBehaviour(FireEngineBehaviour.this);
				}
			 });
		}
		
	}
	private class ReceiveResponse extends SimpleBehaviour{

		boolean finished = false;
		public ReceiveResponse() {
			// TODO Auto-generated constructor stub
		}
		@Override
		public void action() {
			System.out.println("Emergency Response: Result received");
			ACLMessage msg = agent.receive();
			if (msg == null) {
				System.out.println("Emergency Receiver: New message received but its NULL");
				block();
				return;
			}
			try {
				System.out.println("Emergency Receiver: New message received....Message ok Probing");
				Object content = msg.getContentObject();
				switch (msg.getPerformative()) {
				case ACLMessage.INFORM:
					if (content instanceof EQRRoutingResult){
						System.out.println("Emergency Recv: New message received....Message understood");
						handle(msg);
						finished = true;
						return;
					}
					else{
						System.out.println("Emergency Recv: Reply from router not understood");
						finished = true;
					}
				default:
					System.out.println("Emergency Recv: Wrong msg from router");
					finished = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
		
		private void handle(ACLMessage msg){
			System.out.println("Emergency Recv: Handling reply from server");
			try {
				EQRRoutingResult result = (EQRRoutingResult)msg.getContentObject();
				if(result instanceof EQRRoutingError){
					System.out.println("Emergency Recv: Requested Route has errors");
					/*
					 * No Route to Place
					 */
				}
				else if(result instanceof EQRGraphHopperResult){
					
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
