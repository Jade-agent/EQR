package org.nkigen.eqr.agents.behaviours;

import org.nkigen.eqr.messages.EQRRoutingError;
import org.nkigen.eqr.messages.EQRRoutingResult;
import org.nkigen.maps.routing.EQRPoint;
import org.nkigen.maps.routing.graphhopper.EQRGraphHopperResult;
import org.nkigen.maps.viewer.EQRViewer;
import org.nkigen.maps.viewer.EQRViewerPoint;
import org.nkigen.maps.viewer.updates.EQRStatusPanelItem;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;

import jade.core.AID;
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
	HashMap<AID, EQRViewerPoint> static_points;
	HashMap<AID, MarkerViewerPoint> dynamic_points;

	public ViewerBehaviour(Agent agent) {
		// TODO Auto-generated constructor stub
		super(agent);
		viewer = new EQRViewer();
		viewer.setVisible(true);
		static_points = new HashMap<AID, EQRViewerPoint>();
		dynamic_points = new HashMap<AID, MarkerViewerPoint>();
		System.out.println("Viewer Up and running");
	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		if (msg == null) {
			System.out.println(getBehaviourName() + " : "
					+ myAgent.getLocalName()
					+ ": New message received but its NULL");
			block();
			return;
		}
		System.out
				.println("EQRViewer: New message received....Message ok Probing");
		myAgent.addBehaviour(new HandlerBehaviour(msg));

	}

	class HandlerBehaviour extends OneShotBehaviour {

		ACLMessage msg;

		public HandlerBehaviour(ACLMessage msg) {
			super(ViewerBehaviour.this.myAgent);
			this.msg = msg;
		}

		public void action() {
			try {
				System.out.println(getBehaviourName()
						+ ": New message received....Message ok Probing");
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
			AID id = point.getItemId();
			MarkerViewerPoint mp = new MarkerViewerPoint();
			if (couldBeStatic(point)) {
				if (!static_points.containsKey(id)) {
					static_points.put(id, point);
					viewer.addMarker(point);
				}
			} else {
				if (!dynamic_points.containsKey(id)) {
					MapMarkerDot mark = viewer.addMarker(point);
					mp.setMarker(mark);
					mp.setPoint(point);
					dynamic_points.put(id, mp);
				} else {
					viewer.removeMarker(dynamic_points.get(id).getMarker());
					MapMarkerDot mark = viewer.addMarker(point);
					mp.setMarker(mark);
					mp.setPoint(point);
					dynamic_points.put(id, mp);
				}
			}
		}

		private boolean couldBeStatic(EQRViewerPoint point) {
			return (point.getType() == EQRStatusPanelItem.STATIC_FIRE
					|| point.getType() == EQRStatusPanelItem.STATIC_PATIENT || point
						.getType() == EQRStatusPanelItem.HOSPITAL_LOCATION_ITEM);
		}

	}

	private class MarkerViewerPoint {
		EQRViewerPoint point;
		MapMarkerDot mp;

		public MarkerViewerPoint() {

		}

		public MarkerViewerPoint(EQRViewerPoint p, MapMarkerDot m) {
			this.mp = m;
			this.point = p;
		}

		public EQRViewerPoint getPoint() {
			return point;
		}

		public void setPoint(EQRViewerPoint point) {
			this.point = point;
		}

		public MapMarkerDot getMarker() {
			return mp;
		}

		public void setMarker(MapMarkerDot mp) {
			this.mp = mp;
		}
	}
}
