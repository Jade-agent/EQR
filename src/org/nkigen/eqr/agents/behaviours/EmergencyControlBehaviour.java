package org.nkigen.eqr.agents.behaviours;

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

import java.util.ArrayList;
import java.util.List;
import jade.util.Logger;

import org.nkigen.eqr.ambulance.AmbulanceDetails;
import org.nkigen.eqr.common.EQRAgentTypes;
import org.nkigen.eqr.common.EmergencyDetails;
import org.nkigen.eqr.common.EmergencyResponseBase;
import org.nkigen.eqr.common.EmergencyStateChangeListener;
import org.nkigen.eqr.emergencycontrol.EmergencyControlCenterGoals;
import org.nkigen.eqr.logs.EQRLogger;
import org.nkigen.eqr.messages.AmbulanceRequestMessage;
import org.nkigen.eqr.messages.BaseRouteMessage;
import org.nkigen.eqr.messages.ControlCenterInitMessage;
import org.nkigen.eqr.messages.FireEngineRequestMessage;
import org.nkigen.eqr.messages.HospitalRequestMessage;

public class EmergencyControlBehaviour extends CyclicBehaviour implements
		EmergencyStateChangeListener {

	/* TODO: Modify this */
	List<EmergencyResponseBase> ambulance_bases;
	List<EmergencyResponseBase> hospital_bases;
	List<EmergencyResponseBase> fire_engine_bases;
	ArrayList<AID> ambulances;
	ArrayList<AID> fire_engines;
	Logger logger;
	EmergencyControlCenterGoals goals;
	boolean is_setup_complete = false;

	public EmergencyControlBehaviour(Agent a) {
		super(a);
		// EmergencyStateChangeInitiator.getInstance().addListener(this);
		logger = EQRLogger.prep(logger, myAgent.getLocalName());
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
		EQRLogger.log(logger, msg, myAgent.getLocalName(), getBehaviourName()+": Message received");
		//logger.info("message received: "+ msg.getSender());
		switch (msg.getPerformative()) {
		case ACLMessage.REQUEST:
			if (is_setup_complete)
				handleRequestMessage(msg);
			else
				myAgent.send(msg);
			break;
		case ACLMessage.INFORM:
			try {
				Object content = msg.getContentObject();
				if (content instanceof ControlCenterInitMessage) {
					System.out.println(getBehaviourName()
							+ " Init Message recvd");
					initControlCenter((ControlCenterInitMessage) content);
					is_setup_complete = true;
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
		System.out.println(getBehaviourName() + " " + myAgent.getLocalName()
				+ ": amb_b " + ambulance_bases.size() + " fireb "
				+ fire_engine_bases.size() + " :nfb "
				+ ambulance_bases.get(0).getAvailable().size() + " hb "
				+ hospital_bases.get(0).getAvailable().size());
	}

	private void handleRequestMessage(ACLMessage msg) {
		try {
			Object content = msg.getContentObject();
			if (content instanceof AmbulanceRequestMessage) {
				Object[] params = new Object[3];
				params[0] = myAgent;
				params[1] = ((AmbulanceRequestMessage) content).getPatient();
				System.out.println(" Ambulance " + ambulance_bases.size());
				params[2] = getAvailableAmbulanceBases();
				Behaviour b = goals
						.executePlan(
								EmergencyControlCenterGoals.ASSIGN_AMBULANCE_TO_PATIENT,
								params);
				if (b != null)
					myAgent.addBehaviour(b);
			} else if (content instanceof HospitalRequestMessage) {
				if (((HospitalRequestMessage) content).getType() == HospitalRequestMessage.HOSPITAL_REQUEST) {
					System.out
							.println(myAgent.getLocalName()
									+ " Hospital request received from "
									+ ((AmbulanceDetails) ((HospitalRequestMessage) content)
											.getMessage()[0]).getAID());
					Object[] params = new Object[3];
					params[0] = myAgent;
					params[1] = ((HospitalRequestMessage) content).getMessage()[0];
					System.out.println(" Hospitals " + hospital_bases.size());
					params[2] = hospital_bases;
					Behaviour b = goals.executePlan(
							EmergencyControlCenterGoals.GET_NEAREST_HOSPITAL,
							params);
					if (b != null)
						myAgent.addBehaviour(b);
				}
			} else if (content instanceof BaseRouteMessage) {
				BaseRouteMessage brm = (BaseRouteMessage) content;
				if (brm.getType() == BaseRouteMessage.REQUEST) {
					System.out.println(myAgent.getLocalName()
							+ " Back to base msg received");
					Object[] params = new Object[3];
					params[0] = myAgent;
					params[1] = msg.createReply();
					params[2] = brm;
					Behaviour b = goals.executePlan(
							EmergencyControlCenterGoals.GET_RESPONDER_TO_BASE,
							params);
					if (b != null)
						myAgent.addBehaviour(b);
				
				}
			}
			else if(content instanceof FireEngineRequestMessage){
				FireEngineRequestMessage fer = (FireEngineRequestMessage) content;
				if(fer.getType() == FireEngineRequestMessage.REQUEST){
					System.out.println(myAgent.getLocalName()
							+ " Fire Engine request Message received");
					Object[] params = new Object[3];
					params[0] = myAgent;
					params[1] = fer.getFire();
					params[2] = getAvailableFireEngineBases();
					Behaviour b = goals.executePlan(
							EmergencyControlCenterGoals.ASSIGN_FIREENGINE_TO_FIRE,
							params);
					if (b != null)
						myAgent.addBehaviour(b);
				}
			}
		} catch (UnreadableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private List<EmergencyResponseBase> getAvailableFireEngineBases() {
		ArrayList<EmergencyResponseBase> avail = new ArrayList<EmergencyResponseBase>();
		for (EmergencyResponseBase b : fire_engine_bases)
			if (b.getAvailable().size() > 0)
				avail.add(b);
		if (avail.size() > 0)
			return avail;
		return null;
	}
	private List<EmergencyResponseBase> getAvailableAmbulanceBases() {
		ArrayList<EmergencyResponseBase> avail = new ArrayList<EmergencyResponseBase>();
		for (EmergencyResponseBase b : ambulance_bases)
			if (b.getAvailable().size() > 0)
				avail.add(b);
		if (avail.size() > 0)
			return avail;
		return null;
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
