package org.nkigen.eqr.simulation;

import java.util.ArrayList;

import org.nkigen.eqr.common.EQRAgentTypes;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class SimulationBehaviour extends CyclicBehaviour {

	ArrayList<AID> patients;
	ArrayList<AID> fires;

	public SimulationBehaviour(Agent a) {
		super(a);
		initPatients();
		initFires();
	}

	@Override
	public void action() {

	}

	private void initPatients() {
		patients = new ArrayList<AID>();

		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(EQRAgentTypes.PATIENT_AGENT);
		template.addServices(sd);
		try {
			DFAgentDescription[] result = DFService.search(myAgent, template);
			patients.clear();
			System.out.println(result.length);
			for (int i = 0; i < result.length; ++i) {
				patients.add(result[i].getName());
				System.out.println(result[i].getName().getLocalName()
						+ " Added to patients");
			}
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}

	}

	private void initFires() {
		fires = new ArrayList<AID>();
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType(EQRAgentTypes.FIRE_AGENT);
		template.addServices(sd);
		try {
			DFAgentDescription[] result = DFService.search(myAgent, template);
			fires.clear();
			for (int i = 0; i < result.length; ++i) {
				fires.add(result[i].getName());
				System.out.println(result[i].getName().getLocalName()
						+ " Added to fires");
			}
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

}
