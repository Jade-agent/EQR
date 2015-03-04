package org.nkigen.eqr.agents.behaviours;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

import java.util.HashMap;

import org.nkigen.eqr.logs.EQRLogger;
import org.nkigen.eqr.messages.EQRLocationUpdate;
import org.nkigen.maps.viewer.EQRViewer;
import org.nkigen.maps.viewer.EQRViewerPoint;
import org.openstreetmap.gui.jmapviewer.MapMarkerDot;

public class ViewerBehaviour extends CyclicBehaviour {

	EQRViewer viewer;
	HashMap<AID, MarkerViewerPoint> static_points;
	HashMap<AID, MarkerViewerPoint> dynamic_points;
	Logger logger;
	public ViewerBehaviour(Agent agent) {
		// TODO Auto-generated constructor stub
		super(agent);
		viewer = new EQRViewer();
		viewer.setVisible(true);
		logger = EQRLogger.prep(logger, myAgent.getLocalName());
		static_points = new HashMap<AID, MarkerViewerPoint>();
		dynamic_points = new HashMap<AID, MarkerViewerPoint>();
		System.out.println("Viewer Up and running");
	}

	@Override
	public void action() {
		ACLMessage msg = myAgent.receive();
		
		if (msg == null) {
			block();
			return;
		}
		EQRLogger.log(logger, msg, myAgent.getLocalName(), "Message received");
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
				EQRLogger.log(logger, msg, myAgent.getLocalName(), "static point for "+ point.getItemId().getLocalName());
				if (!static_points.containsKey(id)) {
					EQRLogger.log(logger, msg, myAgent.getLocalName(), "NEW static point added for "+ point.getItemId().getLocalName());
					System.out.println(getBehaviourName()+" "+myAgent.getLocalName()+" new static point added");
					MapMarkerDot mark = viewer.addMarker(point);
					mp.setMarker(mark);
					mp.setPoint(point);
					static_points.put(id, mp);
					viewer.addMarker(point);
				}
				else{
					EQRLogger.log(logger, msg, myAgent.getLocalName(), "Static point replaced static point for "+ point.getItemId().getLocalName());
					viewer.removeMarker(static_points.get(id).getMarker());
					EQRViewerPoint p = static_points.get(id).getPoint();
					p.setColor(point.getColor());
					MapMarkerDot mark = viewer.addMarker(p);
					mp.setMarker(mark);
				}
			} else {
				if (!dynamic_points.containsKey(id)) {
				//	EQRLogger.log(logger, msg, myAgent.getLocalName(), "New Dynamic point for "+ point.getItemId().getLocalName());
					MapMarkerDot mark = viewer.addMarker(point);
					mp.setMarker(mark);
					mp.setPoint(point);
					dynamic_points.put(id, mp);
				} else {
					EQRLogger.log(logger, msg, myAgent.getLocalName(), "Updated dynamic point for "+ point.getItemId().getLocalName());
					viewer.removeMarker(dynamic_points.get(id).getMarker());
					MapMarkerDot mark = viewer.addMarker(point);
					mp.setMarker(mark);
					mp.setPoint(point);
					dynamic_points.put(id, mp);
				}
			}
		}

		private boolean couldBeStatic(EQRViewerPoint point) {
			return (point.getType() == EQRLocationUpdate.HOSPITAL_LOCATION
					|| point.getType() == EQRLocationUpdate.PATIENT_LOCATION);
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
