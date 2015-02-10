package org.nkigen.eqr.agents.behaviours;

import org.nkigen.eqr.agents.ontologies.routing.EQRRoutingError;
import org.nkigen.eqr.agents.ontologies.routing.EQRRoutingResult;
import org.nkigen.maps.routing.EQRPoint;
import org.nkigen.maps.routing.graphhopper.EQRGraphHopperResult;
import org.nkigen.maps.viewer.EQRViewer;
import org.nkigen.maps.viewer.EQRViewerPoint;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.UnreadableException;

import java.awt.Color;
import java.util.List;
public class ViewerBehaviour extends CyclicBehaviour {

	EQRViewer viewer;
	private Agent agent;

	public ViewerBehaviour(Agent agent) {
		// TODO Auto-generated constructor stub
		super(agent);
		this.agent = agent;
		viewer = new EQRViewer();
		viewer.setVisible(true);
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
		try {
			Object content = msg.getContentObject();
			switch (msg.getPerformative()) {
			case ACLMessage.REQUEST:
				agent.addBehaviour(new HandlerBehaviour(msg));
				break;
			}
		} catch (UnreadableException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

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
				case ACLMessage.REQUEST:
					if (content instanceof EQRRoutingResult) {
						System.out
								.println("EQRViewer: New message received....Message understood");
						handle(msg);
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

		private void handle(ACLMessage msg) {
			System.out.println("EQRViewer: Handling reply from server");
			try {
				EQRRoutingResult result = (EQRRoutingResult) msg
						.getContentObject();
				if (result instanceof EQRGraphHopperResult) {
					updateViewer((EQRGraphHopperResult) result);
				} else {
					System.out
							.println("EQRViewer: Error...Not updating viewer");
				}
			} catch (UnreadableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void updateViewer(EQRGraphHopperResult result) {
			long _sleep_time =(long) result.getDistance()/result.getDuration();
			
			List<EQRPoint> points = result.getPoints();
			
			for(int i=0; i<points.size();i++){
				EQRViewerPoint p = new EQRViewerPoint(points.get(i), Color.RED);
				viewer.addMarker(p);
				block(_sleep_time);
			}
		}
		
	}
}
