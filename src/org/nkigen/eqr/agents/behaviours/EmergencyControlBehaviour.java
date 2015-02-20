package org.nkigen.eqr.agents.behaviours;

import java.util.ArrayList;

import org.nkigen.eqr.common.EQRAgentTypes;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class EmergencyControlBehaviour extends CyclicBehaviour {

	ArrayList<AID> ambulances;
	ArrayList<AID> fire_engines;

	public EmergencyControlBehaviour(Agent a) {
		super(a);
		initAmbulances();
		initFireEngines();
	}

	@Override
	public void action() {

	}

	private void initAmbulances() {
		ambulances = new ArrayList<AID>();

		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(EQRAgentTypes.AMBULANCE_AGENT);
		template.addServices(sd);
		try {
			DFAgentDescription[] result = DFService.search(myAgent, template);
			ambulances.clear();
			System.out.println(result.length);
			for (int i = 0; i < result.length; ++i) {
				ambulances.add(result[i].getName());
				System.out.println(result[i].getName().getLocalName() +" Added to ambulances");
			}
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

	}

	private void initFireEngines() {
		fire_engines = new ArrayList<AID>();
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(EQRAgentTypes.FIRE_ENGINE_AGENT);
		template.addServices(sd);
		try {
			DFAgentDescription[] result = DFService.search(myAgent, template);
			fire_engines.clear();
			for (int i = 0; i < result.length; ++i) {
				fire_engines.add(result[i].getName());
				System.out.println(result[i].getName().getLocalName() +" Added to fire engines");
			}
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

}
