package org.nkigen.eqr.agents;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

public class RegisterInDF extends OneShotBehaviour {

	Agent agent;
	String type;
	String ownership;
	RegisterInDF(Agent a,String type, String ownership) {
        super(a);
        agent = a;
        this.type = type;
        this.ownership = ownership;
     }

     public void action() {

        ServiceDescription sd = new ServiceDescription();
        sd.setType(type);
        sd.setName(agent.getName());
        sd.setOwnership(ownership); /*TODO*/
        DFAgentDescription dfd = new DFAgentDescription();
        dfd.setName(agent.getAID());
        dfd.addServices(sd);
        try {
           DFAgentDescription[] dfds = DFService.search(agent, dfd);
           if (dfds.length > 0 ) {
              DFService.deregister(agent, dfd);
           }
           DFService.register(agent, dfd);
           System.out.println(agent.getLocalName() + " is ready.");
        }
        catch (Exception ex) {
           System.out.println("Failed registering with DF! Shutting down...");
           ex.printStackTrace();
           agent.doDelete();
        }
     }
}
