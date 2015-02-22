package org.nkigen.eqr.simulation;

import java.io.IOException;
import java.util.ArrayList;

import org.nkigen.eqr.common.EQRAgentTypes;
import org.nkigen.eqr.messages.PatientInitMessage;
import org.nkigen.eqr.patients.PatientDetails;
import org.nkigen.maps.routing.EQRPoint;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

public class SimulationBehaviour extends CyclicBehaviour {

	ArrayList<AID> patients;
	ArrayList<AID> fires;
	ArrayList<AID> ambulances;
	ArrayList<AID> fire_engines;
	ArrayList<AID> hospitals;
	
	boolean init_complete = false;
	String config;
	SimulationGoals goals;

	public SimulationBehaviour(Agent a, String config) {
		super(a);
		this.config = config;
		goals = new SimulationGoals();
		initPatients();
		initFires();
	}

	@Override
	public void action() {
		/* Execute init plan */
		if (!init_complete) {
			Object[] params = new Object[2];
			params[0] = myAgent;
			params[1] = config;
			Behaviour b = goals.executePlan(SimulationGoals.INIT_SIMULATION,
					params);
			if (b != null) {
				myAgent.addBehaviour(b);
			} else {
				System.out.println(getBehaviourName() + "init failed!!");
			}
			return;
		}
		ACLMessage msg = myAgent.receive();
		if(msg != null){
			switch(msg.getPerformative()){
			case ACLMessage.INFORM:
				try {
					Object content = msg.getContentObject();
					if(content instanceof SimulationParamsMessage){
						initSimulation((SimulationParamsMessage) content);
					}
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			break;
			}
		}
		else{
			block();
		}

	}

	private void initSimulation(SimulationParamsMessage params){
		
		
		init_complete = true;
	}
	
	private void setupPatients() {
		if (patients != null) {
			for (int i = 0; i < patients.size(); i++) {
				PatientDetails pd = new PatientDetails();
				pd.setAID(patients.get(i));
				pd.setId(i);
				pd.setLocation(new EQRPoint(0.0, 0.0));
				PatientInitMessage m = new PatientInitMessage();
				m.setPatient(pd);
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				try {
					msg.setContentObject(m);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				msg.addReceiver(pd.getAID());
				myAgent.send(msg);
			}
		}
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
