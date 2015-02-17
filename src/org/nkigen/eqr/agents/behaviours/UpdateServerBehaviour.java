package org.nkigen.eqr.agents.behaviours;

import org.nkigen.eqr.messages.EQRLocationUpdate;
import org.nkigen.eqr.messages.EQRRoutingCriteria;
import org.nkigen.maps.viewer.updates.EQRAmbulanceLocations;
import org.nkigen.maps.viewer.updates.EQRFireEngineLocation;
import org.nkigen.maps.viewer.updates.EQRFiresUpdatesItem;
import org.nkigen.maps.viewer.updates.EQRStatusPanelItem;
import org.nkigen.maps.viewer.updates.EQRUpdateWindow;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class UpdateServerBehaviour extends CyclicBehaviour{

	Agent agent;
	public UpdateServerBehaviour(Agent agent) {
		super(agent);
		this.agent = agent;
	}
	
	@Override
	public void action() {
		ACLMessage msg = agent.receive();
		if (msg == null) {
			System.out.println("Router: New message received but its NULL");
			block();
			return;
		}
		System.out.println("Update Server: New message received");
		try {
			System.out.println("Update Server: New message received....Message ok Probing");
			Object content = msg.getContentObject();
			switch (msg.getPerformative()) {
			case ACLMessage.PROPAGATE:
				if(content instanceof EQRLocationUpdate){
					System.out.println("Update Server: Handling a location update");
					agent.addBehaviour(new HandleLocationUpdate((EQRLocationUpdate)content));
				}
				break;
				default:
					break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private class HandleLocationUpdate extends OneShotBehaviour{
		EQRLocationUpdate msg;
		EQRUpdateWindow win;
		public HandleLocationUpdate(EQRLocationUpdate msg) {
		this.msg = msg;
		win = EQRUpdateWindow.getInstance();
		}
		@Override
		public void action() {
			sendToUpdateWindow();
			
		}
		
		private void sendToUpdateWindow(){
			int type = msg.getType();
			EQRStatusPanelItem update;
			
			switch(type){
			case EQRLocationUpdate.AMBULANCE_LOCATION:
				update = new EQRAmbulanceLocations();
				((EQRAmbulanceLocations)update).setItem_id(msg.getItemId());
				((EQRAmbulanceLocations)update).setHeading(msg.getHeading());
				((EQRAmbulanceLocations)update).setLocation(msg.getCurrent());
				win.newItem(EQRStatusPanelItem.AMBULANCE_LOCATION_ITEM, update);
				break;
			case EQRLocationUpdate.FIRE_ENGINE_LOCATION:
				update = new EQRAmbulanceLocations();
				((EQRFireEngineLocation)update).setItem_id(msg.getItemId());
				((EQRFireEngineLocation)update).setHeading(msg.getHeading());
				((EQRFireEngineLocation)update).setLocation(msg.getCurrent());
				win.newItem(EQRStatusPanelItem.FIRE_ENGINE_LOCATION_ITEM, update);
				break;
			case EQRLocationUpdate.FIRE_LOCATION:
				/*Get Closest Engines*/
				break;
			case EQRLocationUpdate.PATIENT_LOCATION:
				/*Get Closest Ambulance*/
				break;
			}
			
			
		}
		
	}

}
