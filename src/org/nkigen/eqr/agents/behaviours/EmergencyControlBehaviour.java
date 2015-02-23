package org.nkigen.eqr.agents.behaviours;

import java.util.ArrayList;
import java.util.List;

import org.nkigen.eqr.common.EQRAgentTypes;
import org.nkigen.eqr.common.EmergencyDetails;
import org.nkigen.eqr.common.EmergencyResponseBase;
import org.nkigen.eqr.common.EmergencyStateChangeInitiator;
import org.nkigen.eqr.common.EmergencyStateChangeListener;
import org.nkigen.eqr.emergencycontrol.AssignAmbulanceBehaviour;
import org.nkigen.eqr.emergencycontrol.EmergencyControlCenterGoals;
import org.nkigen.eqr.messages.AmbulanceRequestMessage;
import org.nkigen.eqr.messages.ControlCenterInitMessage;
import org.nkigen.eqr.messages.PatientInitMessage;

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

public class EmergencyControlBehaviour extends CyclicBehaviour implements
		EmergencyStateChangeListener {

	/* TODO: Modify this */
	List<EmergencyResponseBase> ambulance_bases;
	List<EmergencyResponseBase> hospital_bases;
	List<EmergencyResponseBase> fire_engine_bases;
	ArrayList<AID> ambulances;
	ArrayList<AID> fire_engines;

	EmergencyControlCenterGoals goals;

	public EmergencyControlBehaviour(Agent a) {
		super(a);
		EmergencyStateChangeInitiator.getInstance().addListener(this);
		goals = new EmergencyControlCenterGoals();
		initAmbulances();
		initFireEngines();
	}

	@Override
	public void action() {

		ACLMessage msg = myAgent.receive();
		if (msg == null) {
			block();
			return;
		}
		switch (msg.getPerformative()) {
		case ACLMessage.REQUEST:
			handleRequestMessage(msg);
			break;
		case ACLMessage.INFORM:
			try {
				Object content = msg.getContentObject();
				if (content instanceof ControlCenterInitMessage) {
					System.out.println(getBehaviourName()
							+ " Init Message recvd");
					initControlCenter((ControlCenterInitMessage) content);

				}
			} catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}
	}

	private void initControlCenter(ControlCenterInitMessage msg) {
		ambulance_bases = msg.getAmbulance_bases();
		fire_engine_bases = msg.getFire_engine_bases();
		hospital_bases = msg.getHospital_bases();
	}

	private void handleRequestMessage(ACLMessage msg) {
		try {
			Object content = msg.getContentObject();
			if (content instanceof AmbulanceRequestMessage) {
				Object[] params = new Object[3];
				params[0] = myAgent;
				params[1] = ((AmbulanceRequestMessage) content).getPatient();
				params[2] = getBases();
				Behaviour b = goals
						.executePlan(
								EmergencyControlCenterGoals.ASSIGN_AMBULANCE_TO_PATIENT,
								params);
				if (b != null)
					myAgent.addBehaviour(b);
			}
		} catch (UnreadableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private List<EmergencyResponseBase> getBases() {
		return null; /* TODO */
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
				System.out.println(result[i].getName().getLocalName()
						+ " Added to ambulances");
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
				System.out.println(result[i].getName().getLocalName()
						+ " Added to fire engines");
			}
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
	}

	@Override
	public void onEmergencyStateChange(EmergencyDetails ed) {
		// TODO Auto-generated method stub

	}

}
