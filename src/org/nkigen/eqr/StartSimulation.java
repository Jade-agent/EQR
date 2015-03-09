package org.nkigen.eqr;

import org.nkigen.eqr.simulation.SimulationAgent;

import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
//import jade.wrapper.StaleProxyException;

/**
 * Contains the main method that starts the simulation
 * 
 * @author nkigen
 *
 */
public class StartSimulation {
	/**
	 * The main method that starts the {@link SimulationAgent}
	 * 
	 * @param args
	 *            The input parameters to the {@link SimulationAgent}
	 */
	public static void main(String[] args) {

		if (args.length == 0) {
			System.out.println("ERROR: Please specify the config file! " + args.length);
			System.exit(-1);
		}
		System.out.println("About to start simulation..");
		jade.core.Runtime rt = Runtime.instance();
		rt.setCloseVM(true);
		System.out.print("runtime created\n");
		Profile profile = new ProfileImpl(null, 1228, null);
		System.out
				.println("Launching a whole in-process platform..." + profile);
		jade.wrapper.AgentContainer mainContainer = rt
				.createMainContainer(profile);

		ProfileImpl pContainer = new ProfileImpl(null, 1261, null);
		jade.wrapper.AgentContainer cont = rt.createAgentContainer(pContainer);
		System.out.println("Launching the Main agent container ..."
				+ pContainer);

		System.out.println("Main Container created");
		System.out.println("Launching the rma agent on the main container ...");
		AgentController rma;
		AgentController sim;
		try {
			rma = mainContainer.createNewAgent("rma", "jade.tools.rma.rma",
					new Object[0]);
			Object[] params = new Object[1];
			params[0] = args[0];
			sim = cont.createNewAgent("simulation_agent", SimulationAgent.class.getName(),
					params);
			rma.start();
			System.out
					.println("Launching the rma agent on the main container ...");
			sim.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
