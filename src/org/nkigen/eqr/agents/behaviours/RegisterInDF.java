package org.nkigen.eqr.agents.behaviours;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class RegisterInDF extends OneShotBehaviour {

	Agent agent;
	String type;
	String ownership;
	public RegisterInDF(Agent a,String type, String ownership) {
        super(a);
        agent = a;
        this.type = type;
        this.ownership = ownership;
     }

     public void action() {

        ServiceDescription sd = new ServiceDescription();
        sd.setType(type);
        sd.setName(agent.getLocalName()+"-"+type);
        //sd.setOwnership(ownership); /*TODO*/
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(agent.getAID());
        dfd.addServices(sd);
        try {
        	
           DFService.register(agent, dfd);
           System.out.println("New Agent of type " + type + " is ready.");
        }
        catch (Exception ex) {
           System.out.println("Failed registering "+ type +" with DF! Shutting down...");
           ex.printStackTrace();
           agent.doDelete();
        }
     }
}
