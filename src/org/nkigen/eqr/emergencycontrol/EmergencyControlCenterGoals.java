package org.nkigen.eqr.emergencycontrol;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nkigen.eqr.agents.EmergencyControlCenterAgent;
import org.nkigen.eqr.ambulance.AmbulanceDetails;
import org.nkigen.eqr.common.EQRGoal;
import org.nkigen.eqr.common.EmergencyResponseBase;
import org.nkigen.eqr.fires.FireDetails;
import org.nkigen.eqr.messages.BaseRouteMessage;
import org.nkigen.eqr.patients.PatientDetails;

public class EmergencyControlCenterGoals extends EQRGoal {

	public static final int ASSIGN_AMBULANCE_TO_PATIENT = 1;
	public static final int ASSIGN_FIREENGINE_TO_FIRE = 2;
	public static final int GET_NEAREST_HOSPITAL = 3;
	public static final int GET_RESPONDER_TO_BASE = 4;
	/* More Goals to be added */

	Map<Integer, Behaviour> goals;

	public EmergencyControlCenterGoals() {
		goals = new HashMap<Integer, Behaviour>();
	}

	public void initPatientGoals(Map<Integer, Behaviour> goals) {
		this.goals = goals;
	}

	public Behaviour executePlan(int which, Object params[]) {
		switch (which) {
		case ASSIGN_AMBULANCE_TO_PATIENT:
			return assignAmbulance(params);
		case ASSIGN_FIREENGINE_TO_FIRE:
			return assignFireEngine(params);
		case GET_NEAREST_HOSPITAL:
			return getNearestHospitalPlan(params);
		case GET_RESPONDER_TO_BASE:
			return getResponderToBasePlan(params);
		default:
			System.out.println("Plan not available to achieve goal " + which);
			return null;
		}
	}

	private Behaviour getResponderToBasePlan(Object p[]) {
		if (p.length == 3) {
			if (p[0] instanceof EmergencyControlCenterAgent
					&& p[1] instanceof ACLMessage && p[2] instanceof BaseRouteMessage) 
				return new ResponderToBaseBehaviour(
						(EmergencyControlCenterAgent) p[0],
						(ACLMessage) p[1], (BaseRouteMessage) p[2]);
		}
		System.out.println("Wrong Number of params ");
		return null;
	}
	private Behaviour getNearestHospitalPlan(Object p[]) {
		if (p.length == 3) {
			if (p[0] instanceof EmergencyControlCenterAgent
					&& p[1] instanceof AmbulanceDetails && p[2] instanceof List<?>) 
				return new NearestHospitalBehaviour(
						(EmergencyControlCenterAgent) p[0],
						(AmbulanceDetails) p[1], (List<EmergencyResponseBase>) p[2]);
		}
		System.out.println("Wrong Number of params ");
		return null;
	}

	private Behaviour assignAmbulance(Object p[]) {
		
		if (p.length == 3) {
			if (p[0] instanceof EmergencyControlCenterAgent
					&& p[1] instanceof PatientDetails && p[2] instanceof ArrayList<?>) 
				return new AssignAmbulanceBehaviour(
						(EmergencyControlCenterAgent) p[0],
						(PatientDetails) p[1], (List<EmergencyResponseBase>) p[2]);
		}
		System.out.println("Wrong Number of params ");
		return null;
	}

	private Behaviour assignFireEngine(Object p[]) {
		if (p.length == 3) {
			if (p[0] instanceof EmergencyControlCenterAgent
					&& p[1] instanceof FireDetails && p[2] instanceof ArrayList<?>) 
				return new AssignFireEngineBehaviour(
						(EmergencyControlCenterAgent) p[0],
						(FireDetails) p[1], (List<EmergencyResponseBase>) p[2]);
		}
		System.out.println("Wrong Number of params ");
		return null;
	}

	@Override
	public void newGoal(int which, Class behaviour) {
		// TODO Auto-generated method stub
		
	}
}
