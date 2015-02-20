package org.nkigen.eqr.emergencycontrol;

import jade.core.behaviours.Behaviour;

import java.util.HashMap;
import java.util.Map;

import org.nkigen.eqr.agents.EmergencyControlCenterAgent;
import org.nkigen.eqr.common.EQRGoal;
import org.nkigen.eqr.patients.PatientDetails;

public class EmergencyControlCenterGoals extends EQRGoal {

	public static final int ASSIGN_AMBULANCE_TO_PATIENT = 0;
	public static final int ASSIGN_FIREENGINE_TO_FIRE = 1;
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
		default:
			System.out.println("Plan not available to achieve goal " + which);
			return null;
		}
	}

	private Behaviour assignAmbulance(Object p[]) {
		if (p.length == 2) {
			if (p[0] instanceof EmergencyControlCenterAgent
					&& p[1] instanceof PatientDetails)
				return new AssignAmbulanceBehaviour(
						(EmergencyControlCenterAgent) p[0],
						(PatientDetails) p[1]);
		}
		System.out.println("Wrong Number of params ");
		return null;
	}

	private Behaviour assignFireEngine(Object p[]) {
		return null;
	}
}
