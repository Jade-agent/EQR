package org.nkigen.eqr.agents;

import java.util.concurrent.TimeUnit;

import org.nkigen.eqr.models.EmergencyArrivalModel;

import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.TimeInstant;
import jade.core.AID;
import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class EQRAgentsHelper {
	static Experiment experiment;
	static AID update_server;
	static AID routing_server;
	static AID viewer;
	/*
	 * Different types of objects in the experiment
	 */
	// public static

	public static long getCurrentTime() {
		return experiment.getSimClock().getTime().getTimeInEpsilon();

	}

	public static void startSimulation() {

		if (experiment != null) {
			System.out.println("DESMOJ Experiment already started...");
			return;
		}
		experiment = new Experiment("Emergency Resque Model", TimeUnit.SECONDS,
				TimeUnit.MINUTES, null);

		EmergencyArrivalModel vc_1st_p_Model = new EmergencyArrivalModel(null,
				"Emergency Arrival Model", true, false);

		vc_1st_p_Model.connectToExperiment(experiment);

		experiment.tracePeriod(new TimeInstant(0), new TimeInstant(100));
		experiment.setExecutionSpeedRate(1);
		// now set the time this simulation should stop at
		// let him work 1500 Minutes
		experiment.stop(new TimeInstant(1500));
		experiment.setShowProgressBar(false);

		// start the Experiment with start time 0.0
		experiment.start();

		// experiment.report();

		experiment.finish();
	}

	public static AID locateUpdateServer(Agent agent) {

		if (update_server != null)
			return update_server;
		System.out.println("Trying to locate the Update server");
		ServiceDescription sd = new ServiceDescription();
		sd.setType(EQRAgentTypes.UPDATES_AGENT);
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.addServices(sd);
		try {
			DFAgentDescription[] dfds = DFService.search(agent, dfd);
			if (dfds.length > 0) {
				update_server = dfds[0].getName();
				System.out.println("Update Server found");
			} else
				System.out.println("Couldn't localize server!");
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Failed searching int the DF!");
		}
		return update_server;
	}

	public static AID locateRoutingServer(Agent agent) {

		if (routing_server != null)
			return routing_server;
		
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
		return routing_server;
	}

	public static AID locateViewer(Agent agent) {
		if(viewer != null)
			return viewer;
		System.out.println("Trying to locate the Viewer server");
		ServiceDescription sd = new ServiceDescription();
		sd.setType(EQRAgentTypes.VIEWER_AGENT);
		DFAgentDescription dfd = new DFAgentDescription();
		dfd.addServices(sd);
		try {
			DFAgentDescription[] dfds = DFService.search(agent, dfd);
			if (dfds.length > 0) {
				viewer = dfds[0].getName();
				System.out.println("Viewer Server found "
						+ viewer.getLocalName() + " " + viewer.getName());
			} else
				System.out.println("Couldn't locate Viewer server!");
		} catch (Exception ex) {
			ex.printStackTrace();
			System.out.println("Failed for viewer searching int the DF!");
		}
		return viewer;
	}

}
