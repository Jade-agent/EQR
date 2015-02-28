package org.nkigen.eqr.messages;

import jade.core.AID;
import jade.lang.acl.ACLMessage;

import java.io.Serializable;
import java.util.ArrayList;

import org.nkigen.eqr.common.EmergencyResponseBase;
import org.nkigen.maps.routing.EQRPoint;
import org.nkigen.maps.routing.graphhopper.GraphHopperServer;

public class MultipleRoutingRequestMessage implements Serializable {
	AID reply_to;
	GraphHopperServer router;
	ArrayList<EmergencyResponseBase> bases;
	EQRPoint to;
	
	public AID getReply_to() {
		return reply_to;
	}
	public void setReply_to(AID reply_to) {
		this.reply_to = reply_to;
	}
	public GraphHopperServer getRouter() {
		return router;
	}
	public void setRouter(GraphHopperServer router) {
		this.router = router;
	}
	public ArrayList<EmergencyResponseBase> getBases() {
		return bases;
	}
	public void setBases(ArrayList<EmergencyResponseBase> bases) {
		this.bases = bases;
	}
	public EQRPoint getTo() {
		return to;
	}
	public void setTo(EQRPoint to) {
		this.to = to;
	}
}
