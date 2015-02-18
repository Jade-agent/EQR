package org.nkigen.eqr.agents.behaviours;

import org.nkigen.eqr.messages.EQRRoutingError;
import org.nkigen.eqr.messages.EQRRoutingResult;
import org.nkigen.maps.routing.EQRPoint;
import org.nkigen.maps.routing.graphhopper.EQRGraphHopperResult;
import org.nkigen.maps.viewer.EQRViewer;
import org.nkigen.maps.viewer.EQRViewerPoint;
import org.nkigen.maps.viewer.updates.EQRStatusPanelItem;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class ViewerBehaviour extends CyclicBehaviour {

	EQRViewer viewer;
	private Agent agent;

	HashMap<Integer,EQRViewerPoint> static_points;
	HashMap<Integer,EQRViewerPoint> dynamic_points;

	public ViewerBehaviour(Agent agent) {
		// TODO Auto-generated constructor stub
		super(agent);
		this.agent = agent;
		viewer = new EQRViewer();
		viewer.setVisible(true);
		static_points = new HashMap<Integer,EQRViewerPoint>();
		dynamic_points = new HashMap<Integer,EQRViewerPoint>();
		System.out.println("Viewer Up and running");
	}

	@Override
	public void action() {
		System.out.println("EQRViewer: New message received");
		ACLMessage msg = agent.receive();
		if (msg == null) {
			System.out.println("EQRViewer: New message received but its NULL");
			block();
			return;
		}
		System.out
				.println("EQRViewer: New message received....Message ok Probing");
		agent.addBehaviour(new HandlerBehaviour(msg));

	}

	class HandlerBehaviour extends OneShotBehaviour {

		ACLMessage msg;

		public HandlerBehaviour(ACLMessage msg) {
			super(agent);
			this.msg = msg;
		}

		public void action() {
			try {
				System.out
						.println("EQRViewer: New message received....Message ok Probing");
				Object content = msg.getContentObject();
				switch (msg.getPerformative()) {
				case ACLMessage.INFORM:
					if (content instanceof EQRViewerPoint) {
						System.out
								.println("EQRViewer: New message received....Message understood");
						handle((EQRViewerPoint) content);
						return;
					} else {
						System.out
								.println("EQRViewer: Reply from router not understood");
					}
				default:
					System.out.println("EQRViewer: Wrong msg from router");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		private void handle(EQRViewerPoint point) {
			int id = point.getItemId();
			
			if(couldBeStatic(point)){
				if(!static_points.containsKey(id))
					static_points.put(id, point);
			}
			else{
				if(!dynamic_points.containsKey(id))
					dynamic_points.put(id, point);
				else{
					EQRViewerPoint p = (EQRViewerPoint)dynamic_points.get(id);
					//viewer.removeMarker(p);
					//dynamic_points.remove(id);
					System.out.println("MYCOLOR: " + point.getColor());
					dynamic_points.put(id, point);
				}
			}
			viewer.addMarker(point);
		}
		
		private boolean couldBeStatic(EQRViewerPoint point){
			return  (point.getType() == EQRStatusPanelItem.PATIENT_STATUS_ITEM || 
					point.getType() == EQRStatusPanelItem.PATIENT_STATUS_ITEM);
		}

	}
}
