package org.nkigen.eqr.agents.behaviours;

import java.util.concurrent.TimeUnit;

import org.nkigen.eqr.agents.EQRAgentTypes; 
import org.nkigen.eqr.messages.EQRRoutingCriteria;
import org.nkigen.eqr.messages.EQRRoutingError;
import org.nkigen.eqr.messages.EQRRoutingResult;
import org.nkigen.eqr.models.EmergencyArrivalModel;
import org.nkigen.eqr.models.EmergencyHandler;
import org.nkigen.maps.routing.EQRPoint;
import org.nkigen.maps.routing.graphhopper.EQRGraphHopperResult;

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

public class EmergencyResponseBehaviour extends CyclicBehaviour {

	Agent agent;
	private AID routing_server;
	private AID viewer;
	int test = 0;
	EQRRoutingCriteria to_serve;
	public EmergencyResponseBehaviour(Agent agent) {
		super(agent);
		this.agent = agent;
		startSimulation();
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
			locateRoutingServer();
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


	void locateRoutingServer() {
		// --------------------- Search in the DF to retrieve the server AID

		System.out.println("Trying to locate the routing server");
		ServiceDescription sd = new ServiceDescription();
		sd.setType(EQRAgentTypes.ROUTING_AGENT);
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.addServices(sd);
		try {
			DFAgentDescription[] dfds = DFService.search(agent, dfd);
			if (dfds.length > 0) {
				routing_server = dfds[0].getName();
				System.out.println("Router found");
			} else
				System.out.println("Couldn't localize server!");
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Failed searching int the DF!");
		}
	}
	
	void locateViewer() {
		// --------------------- Search in the DF to retrieve the server AID

		System.out.println("Trying to locate the Viewer server");
		ServiceDescription sd = new ServiceDescription();
		sd.setType(EQRAgentTypes.VIEWER_AGENT);
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.addServices(sd);
		try {
			DFAgentDescription[] dfds = DFService.search(agent, dfd);
			if (dfds.length > 0) {
				viewer = dfds[0].getName();
				System.out.println("Viewer Server found "+ viewer.getLocalName() + " " + viewer.getName());
			} else
				System.out.println("Couldn't locate Viewer server!");
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Failed for viewer searching int the DF!");
		}
	}
	
	
	private class WaitRouterResponse extends ParallelBehaviour {
		public WaitRouterResponse(){
		     super(agent, 1);
		     addSubBehaviour(new ReceiveResponse());
		     addSubBehaviour(new WakerBehaviour(agent, 5000) {

			    protected void handleElapsedTimeout() {
				   System.out.println("\n\tNo response from server. Please, try later!");
				   agent.addBehaviour(EmergencyResponseBehaviour.this);
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
				}
				else if(result instanceof EQRGraphHopperResult){
					System.out.println("Emergency Recv: Route ok");
					System.out.println(((EQRGraphHopperResult)result).toString());
					if (viewer == null){
						locateViewer();
						System.out.println("Viewer server located!!");
					}
					if (viewer == null) {
						System.out
								.println("Unable to localize the server! Operation aborted!");
						return;
					}
					ACLMessage msg2 = new ACLMessage(ACLMessage.REQUEST);
					try {
						msg2.setContentObject((java.io.Serializable) result);
						msg2.addReceiver(viewer);
						System.out.println("Contacting Viewer server... Please wait!");
						agent.send(msg2);
						System.out.println("Message send to Viewer ... Please wait!");
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					
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
	
	private void startSimulation() {

		Experiment experiment =
			new Experiment("Vancarrier Model", TimeUnit.SECONDS, TimeUnit.MINUTES, null);

		EmergencyArrivalModel vc_1st_p_Model =
			new EmergencyArrivalModel(
				null,
				"Emergency Arrival Model",
				true,
				false);

		
		vc_1st_p_Model.connectToExperiment(experiment);

		experiment.tracePeriod(new TimeInstant(0), new TimeInstant(100));

		// now set the time this simulation should stop at 
		// let him work 1500 Minutes
		experiment.stop(new TimeInstant(1500));
		experiment.setShowProgressBar(false);

		// start the Experiment with start time 0.0
		experiment.start();


		//experiment.report();

		experiment.finish();
	}

}
