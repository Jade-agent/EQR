package org.nkigen.eqr.agents.behaviours;

import java.util.concurrent.TimeUnit;

import org.nkigen.eqr.agents.EQRAgentTypes;
import org.nkigen.eqr.agents.EQRAgentsHelper;
import org.nkigen.eqr.messages.EQRRoutingCriteria;
import org.nkigen.eqr.messages.EQRRoutingError;
import org.nkigen.eqr.messages.EQRRoutingResult;
import org.nkigen.eqr.models.EQREmergencyPoint;
import org.nkigen.eqr.models.EmergencyArrivalModel;
import org.nkigen.eqr.models.EmergencyHandler;
import org.nkigen.maps.routing.EQRPoint;
import org.nkigen.maps.routing.graphhopper.EQRGraphHopperResult;
import org.nkigen.maps.viewer.updates.EQRUpdateWindow;

import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.TimeInstant;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.WakerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class AmbulanceBehaviour extends CyclicBehaviour {

	Agent agent;
	private AID routing_server;
	private AID viewer;
	EQREmergencyPoint to_serve;

	public AmbulanceBehaviour(Agent agent) {
		super(agent);
		this.agent = agent;
		EQRUpdateWindow.getInstance();//.setVisible(true);
		agent.addBehaviour(new StartSimulation());
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub
		ACLMessage msg = null;
		if (isNewEmergency()) {
			agent.addBehaviour(new NewRequestBehaviour(to_serve));
		} else
			block();
	}

	private boolean isNewEmergency() {
		to_serve = EmergencyHandler.getInstance().getPatientPoint();
		if (to_serve == null)
			return false;
		System.out.println(EQRAgentsHelper.getCurrentTime()+":New Patient Found at "+ to_serve.getLatitude() + ", " +to_serve.getLongitude());
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
			EQREmergencyPoint p = EmergencyHandler.getInstance().getAmbulance(
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

	void sendMessage(int performative, Object content, EQREmergencyPoint ambulance, EQREmergencyPoint patient) {
		// ----------------------------------------------------

		if (routing_server == null) {
			routing_server = EQRAgentsHelper.locateRoutingServer(agent);
			System.out.println("Routing server located!!");
		}
		if (routing_server == null) {
			System.out
					.println("Unable to locate the server! Operation aborted!");
			return;
		}
		ACLMessage msg = new ACLMessage(performative);
		try {
			msg.setContentObject((java.io.Serializable) content);
			msg.addReceiver(routing_server);
			System.out.println("Contacting server... Please wait!");
			agent.send(msg);
			agent.addBehaviour(new WaitRouterResponse(ambulance, patient));
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private class WaitRouterResponse extends ParallelBehaviour {
		public WaitRouterResponse(EQREmergencyPoint ambulance, EQREmergencyPoint patient) {
			super(agent, 1);
			addSubBehaviour(new ReceiveResponse(ambulance, patient));
			addSubBehaviour(new WakerBehaviour(agent, 5000) {

				protected void handleElapsedTimeout() {
					System.out
							.println("\n\tNo response from server. Please, try later!");
					agent.addBehaviour(AmbulanceBehaviour.this);
				}
			});
		}

	}

	private class ReceiveResponse extends SimpleBehaviour {

		boolean finished = false;
		EQREmergencyPoint ambulance;
		EQREmergencyPoint patient;
		public ReceiveResponse(EQREmergencyPoint ambulance, EQREmergencyPoint patient) {
			this.patient = patient;
			this.ambulance = ambulance;
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
					agent.addBehaviour(new AmbulanceManagerBehaviour(agent, result, ambulance, patient));

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
	
	class StartSimulation extends OneShotBehaviour{

		@Override
		public void action() {
			 EQRAgentsHelper.startSimulation();
			 
		}
		
	
	}

}
