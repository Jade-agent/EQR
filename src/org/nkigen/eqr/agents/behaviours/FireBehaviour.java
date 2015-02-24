package org.nkigen.eqr.agents.behaviours;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import org.nkigen.eqr.fires.FireDetails;
import org.nkigen.eqr.messages.AmbulanceInitMessage;
import org.nkigen.eqr.messages.FireInitMessage;

public class FireBehaviour extends CyclicBehaviour {

	FireDetails fd;
	

	public FireBehaviour(Agent agent) {
		super(agent);
	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		if(msg != null){
			switch(msg.getPerformative()){
			case ACLMessage.INFORM:
				try {
					Object content = msg.getContentObject();
					 if(content instanceof FireInitMessage){
						fd = ((FireInitMessage)content).getFire();
						System.out.println(getBehaviourName()+" Fire init recieved "+ myAgent.getLocalName());
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

}
