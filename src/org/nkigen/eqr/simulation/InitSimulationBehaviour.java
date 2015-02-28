package org.nkigen.eqr.simulation;

import java.io.File;
import java.io.IOException;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class InitSimulationBehaviour extends OneShotBehaviour {

	File file;

	public InitSimulationBehaviour(Agent agent, String config) {
		// TODO Auto-generated constructor stub
		super(agent);
		file = new File(config);
	}

	@Override
	public void action() {
		if (file != null) {
			ParseinitXMLFile parser = new ParseinitXMLFile(file);
			parser.Parse();
			SimulationParamsMessage params = parser.getSimulationParams();
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.addReceiver(myAgent.getAID());
			try {
				msg.setContentObject(params);
				myAgent.send(msg);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

}
