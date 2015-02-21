package org.nkigen.eqr.simulation;

import java.io.IOException;
import java.util.ArrayList;

import org.nkigen.eqr.common.EQRAgentTypes;
import org.nkigen.eqr.messages.PatientInitMessage;
import org.nkigen.eqr.patients.PatientDetails;
import org.nkigen.maps.routing.EQRPoint;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;

public class SimulationBehaviour extends CyclicBehaviour {

	ArrayList<AID> patients;
	ArrayList<AID> fires;

	boolean patients_init = false;
	boolean fires_init = false;
	public SimulationBehaviour(Agent a, String config) {
		super(a);
		initPatients();
		initFires();
	}

	@Override
	public void action() {

		if(patients_init == false){
			patients_init = true;
			setupPatients();
		}
	}
	
	private void setupPatients(){
		if(patients != null){
			for(int i=0; i< patients.size();i++){
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
