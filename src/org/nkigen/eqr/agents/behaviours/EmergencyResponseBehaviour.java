package org.nkigen.eqr.agents.behaviours;

import org.nkigen.eqr.agents.EQRAgentTypes;
import org.nkigen.eqr.agents.ontologies.routing.EQRRoutingCriteria;

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

public class EmergencyResponseBehaviour extends CyclicBehaviour {

	Agent agent;
	private AID routing_server;
	public EmergencyResponseBehaviour(Agent agent) {
		super(agent);
		this.agent = agent;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void action() {
		// TODO Auto-generated method stub

		if (isNewEEmergency()) {
			EQRRoutingCriteria loc = getLocation();
			agent.addBehaviour(new NewRequestBehaviour(loc));
		} else
			block();

	}

	private boolean isNewEEmergency() {
		return true;
	}

	private EQRRoutingCriteria getLocation() {
		return null;
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

		if (routing_server == null)
			locateRoutingServer();
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
	
	
	private class WaitRouterResponse extends ParallelBehaviour {
		public WaitRouterResponse(){
		     super(agent, 1);
		     addSubBehaviour(new ReceiveResponse());
		     addSubBehaviour(new WakerBehaviour(myAgent, 5000) {

			    protected void handleElapsedTimeout() {
				   System.out.println("\n\tNo response from server. Please, try later!");
				   agent.addBehaviour(EmergencyResponseBehaviour.this);
				}
			 });
		}
		
	}
	private class ReceiveResponse extends SimpleBehaviour{

		public ReceiveResponse() {
			// TODO Auto-generated constructor stub
		}
		@Override
		public void action() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean done() {
			// TODO Auto-generated method stub
			return false;
		}
		
	}
}
