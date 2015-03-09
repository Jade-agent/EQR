package org.nkigen.eqr.simulation;

import java.io.IOException;
import java.util.ArrayList;

import org.nkigen.eqr.agents.AmbulanceAgent;
import org.nkigen.eqr.agents.EQRAgentsHelper;
import org.nkigen.eqr.agents.EQRFireAgent;
import org.nkigen.eqr.agents.EQRPatientAgent;
import org.nkigen.eqr.agents.FireEngineAgent;
import org.nkigen.eqr.agents.HospitalAgent;
import org.nkigen.eqr.ambulance.AmbulanceDetails;
import org.nkigen.eqr.common.EQRAgentTypes;
import org.nkigen.eqr.common.EmergencyResponseBase;
import org.nkigen.eqr.fireengine.FireEngineDetails;
import org.nkigen.eqr.fires.FireDetails;
import org.nkigen.eqr.hospital.HospitalDetails;
import org.nkigen.eqr.logs.EQRLogger;
import org.nkigen.eqr.messages.AmbulanceInitMessage;
import org.nkigen.eqr.messages.ControlCenterInitMessage;
import org.nkigen.eqr.messages.FireEngineInitMessage;
import org.nkigen.eqr.messages.FireInitMessage;
import org.nkigen.eqr.messages.HospitalInitMessage;
import org.nkigen.eqr.messages.PatientInitMessage;
import org.nkigen.eqr.patients.PatientDetails;
import org.nkigen.maps.routing.EQRPoint;

import jade.core.AID;
import jade.core.Agent;
import jade.core.ProfileImpl;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;
import jade.util.Logger;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import jade.core.Runtime;

public class SimulationBehaviour extends CyclicBehaviour {

	jade.core.Runtime rt;
	ArrayList<AID> patients;
	ArrayList<AID> fires;
	ArrayList<AID> ambulances;
	ArrayList<AID> fire_engines;
	ArrayList<AID> hospitals;
	AID control_center;
	boolean init_complete = false;
	String config;
	SimulationGoals goals;
	Logger logger;

	public SimulationBehaviour(Agent a, String config) {
		super(a);
		System.out.println(config);
		this.config = config;
		goals = new SimulationGoals();
		logger = EQRLogger.prep(logger, myAgent.getLocalName());
	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();

		if (msg != null) {
			EQRLogger.log(logger, msg, myAgent.getLocalName(),
					"message recevied");
			switch (msg.getPerformative()) {
			case ACLMessage.INFORM:
				try {
					Object content = msg.getContentObject();
					if (content instanceof SimulationParamsMessage) {
						initSimulation((SimulationParamsMessage) content);
					}
				} catch (UnreadableException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				break;
			}
		} else {
			block();
		}

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

	}

	private void initSimulation(SimulationParamsMessage params) {
		initPatients(params.getPatients().size());
		initFire(params.getFires().size());
		initHospitals(params.getHospitals().size());
		int nEngines = 0, nAmbulances=0;
		for(EmergencyResponseBase base: params.getFire_engines())
			nEngines += base.getMax();
		for(EmergencyResponseBase base: params.getAmbulances())
			nAmbulances += base.getMax();
		initFireEngines(nEngines);
		initAmbulances(nAmbulances);
		/*
		 * initPatients(params.getPatients().size()); initFires();
		 * setupPatients(params.getPatients()); setupFires(params.getFires());
		 * setupHospitals(params.getHospitals());
		 * setupAmbulances(params.getAmbulances());
		 * setupFireEngines(params.getFire_engines());
		 * initControlCenter(params); init_complete = true;
		 */
		init_complete = true;
	}

	

	private void initControlCenter(SimulationParamsMessage m) {
		control_center = EQRAgentsHelper.locateControlCenter(myAgent);
		ControlCenterInitMessage msg = new ControlCenterInitMessage();
		msg.setAmbulance_bases(m.getAmbulances());
		msg.setFire_engine_bases(m.getFire_engines());
		msg.setHospital_bases(m.getHospitals());
		ACLMessage acl = new ACLMessage(ACLMessage.INFORM);
		try {
			acl.setContentObject(msg);
			acl.addReceiver(control_center);
			EQRLogger.log(logger, acl, myAgent.getLocalName(),
					" init control center");
			myAgent.send(acl);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void setupFireEngines(ArrayList<EmergencyResponseBase> loc) {
		fire_engines = EQRAgentsHelper.locateBases(
				EQRAgentTypes.FIRE_ENGINE_AGENT, myAgent);

		// System.out.println(getBehaviourName()+" "+
		// myAgent.getLocalName()+" fire engines: "+ fire_engines.size()+
		// " loc size" + loc.size());
		if (fire_engines != null) {
			int k = 0;
			for (int j = 0, i = 0; j < loc.size(); j++) {
				// System.out.println(getBehaviourName()+" "+
				// myAgent.getLocalName()+ " MAX value :"+loc.get(j).getMax());

				for (; i < fire_engines.size() && i < loc.get(j).getMax(); i++, k++) {
					loc.get(j).addResponder(fire_engines.get(i));
					System.out.println(getBehaviourName() + " "
							+ myAgent.getLocalName() + " fer added");
					FireEngineDetails pd = new FireEngineDetails();
					pd.setAID(fire_engines.get(i));
					pd.setId(k);
					pd.setLocation(loc.get(j).getLocation());
					pd.setCurrentLocation(loc.get(j).getLocation());
					FireEngineInitMessage m = new FireEngineInitMessage();
					m.setFireEngine(pd);
					ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
					try {
						msg.setContentObject(m);
						msg.addReceiver(pd.getAID());
						EQRLogger.log(logger, msg, myAgent.getLocalName(),
								" setup fire engine : "
										+ pd.getAID().getLocalName());
						myAgent.send(msg);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

	}

	private void setupAmbulances(ArrayList<EmergencyResponseBase> loc) {
		ambulances = EQRAgentsHelper.locateBases(EQRAgentTypes.AMBULANCE_AGENT,
				myAgent);

		if (ambulances != null) {
			int k = 0;
			for (int j = 0, i = 0; j < loc.size(); j++) {
				for (; i < ambulances.size() && i < loc.get(j).getMax(); i++, k++) {
					loc.get(j).addResponder(ambulances.get(i));
					AmbulanceDetails pd = new AmbulanceDetails();
					pd.setAID(ambulances.get(i));

					pd.setId(k);
					pd.setLocation(loc.get(j).getLocation());
					pd.setCurrentLocation(loc.get(j).getLocation());
					AmbulanceInitMessage m = new AmbulanceInitMessage();
					m.setAmbulance(pd);
					ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
					try {
						msg.setContentObject(m);
						msg.addReceiver(pd.getAID());
						EQRLogger.log(logger, msg, myAgent.getLocalName(),
								" setup Ambulance : "
										+ pd.getAID().getLocalName());
						myAgent.send(msg);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}

	}

	private void setupHospitals(ArrayList<EmergencyResponseBase> loc) {
		hospitals = EQRAgentsHelper.locateBases(EQRAgentTypes.HOSPITAL_AGENT,
				myAgent);

		if (hospitals != null) {
			for (int i = 0, j = 0; i < hospitals.size() && j < loc.size(); i++, j++) {
				loc.get(i).addResponder(hospitals.get(i));
				HospitalDetails pd = new HospitalDetails();
				pd.setAID(hospitals.get(i));
				pd.setId(i);
				pd.setLocation(loc.get(j).getLocation());
				HospitalInitMessage m = new HospitalInitMessage();
				m.setHospital(pd);
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				try {
					msg.setContentObject(m);
					msg.addReceiver(pd.getAID());
					EQRLogger.log(logger, msg, myAgent.getLocalName(),
							" setup Hospital : " + pd.getAID().getLocalName());
					myAgent.send(msg);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

	private void setupFires(ArrayList<EQRPoint> loc) {
		if (fires != null) {
			for (int i = 0, j = 0; i < fires.size() && j < loc.size(); i++, j++) {
				FireDetails pd = new FireDetails();
				pd.setAID(fires.get(i));
				pd.setId(i);
				pd.setLocation(loc.get(j));
				FireInitMessage m = new FireInitMessage();
				m.setFire(pd);
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				try {
					msg.setContentObject(m);
					msg.addReceiver(pd.getAID());
					EQRLogger
							.log(logger, msg, myAgent.getLocalName(),
									" setup fire Agent : "
											+ pd.getAID().getLocalName());
					myAgent.send(msg);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	private void setupPatients(ArrayList<EQRPoint> loc) {
		if (patients != null) {
			for (int i = 0, j = 0; i < patients.size() && j < loc.size(); i++, j++) {
				PatientDetails pd = new PatientDetails();
				pd.setAID(patients.get(i));
				pd.setId(i);
				pd.setLocation(loc.get(j));
				PatientInitMessage m = new PatientInitMessage();
				m.setPatient(pd);
				ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
				try {
					msg.setContentObject(m);
					msg.addReceiver(pd.getAID());
					EQRLogger.log(logger, msg, myAgent.getLocalName(),
							" setup Patient Agent : "
									+ pd.getAID().getLocalName());
					myAgent.send(msg);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
	}

	private void initPatients(int num) {
		patients = new ArrayList<AID>();
		rt = Runtime.instance();
		ProfileImpl pContainer = new ProfileImpl(null, 1211, null);
		jade.wrapper.AgentContainer cont = rt.createAgentContainer(pContainer);
		try {
			for (int i = 0; i < num; i++) {

				AgentController pa = cont.acceptNewAgent("patient_" + i,
						new EQRPatientAgent());
				pa.start();

			}
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void initHospitals(int num) {
		hospitals = new ArrayList<AID>();
		rt = Runtime.instance();
		ProfileImpl pContainer = new ProfileImpl(null, 1211, null);
		jade.wrapper.AgentContainer cont = rt.createAgentContainer(pContainer);
		try {
			for (int i = 0; i < num; i++) {

				AgentController pa = cont.acceptNewAgent("hospital_" + i,
						new HospitalAgent());
				pa.start();

			}
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void initFire(int size) {
		fires = new ArrayList<AID>();
		rt = Runtime.instance();
		ProfileImpl pContainer = new ProfileImpl(null, 1211, null);
		jade.wrapper.AgentContainer cont = rt.createAgentContainer(pContainer);
		try {
			for (int i = 0; i < size; i++) {
				AgentController pa = cont.acceptNewAgent("fire_" + i ,
						new EQRFireAgent());
				pa.start();
			}
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void initFireEngines(int size) {
		// TODO Auto-generated method stub
		fire_engines = new ArrayList<AID>();
		rt = Runtime.instance();
		ProfileImpl pContainer = new ProfileImpl(null, 1211, null);
		jade.wrapper.AgentContainer cont = rt.createAgentContainer(pContainer);
		try {
			for (int i = 0; i < size; i++) {
				AgentController pa = cont.acceptNewAgent("fireEngine_" + i ,
						new FireEngineAgent());
				pa.start();
			}
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	private void initAmbulances(int size) {
		// TODO Auto-generated method stub
		fire_engines = new ArrayList<AID>();
		rt = Runtime.instance();
		ProfileImpl pContainer = new ProfileImpl(null, 1211, null);
		jade.wrapper.AgentContainer cont = rt.createAgentContainer(pContainer);
		try {
			for (int i = 0; i < size; i++) {
				AgentController pa = cont.acceptNewAgent("ambulance_" + i ,
						new AmbulanceAgent());
				pa.start();
			}
		} catch (StaleProxyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
