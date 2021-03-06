package org.nkigen.eqr.ambulance;

import jade.core.behaviours.Behaviour;

import org.nkigen.eqr.agents.AmbulanceAgent;
import org.nkigen.eqr.common.EQRGoal;
import org.nkigen.eqr.messages.AmbulanceNotifyMessage;
import org.nkigen.eqr.messages.TrafficUpdateMessage;
import org.nkigen.eqr.patients.PatientDetails;

public class AmbulanceGoals extends EQRGoal {

	public static final int AMBULANCE_IDLE = 0; /* Not really a goal :-) */
	public static final int PICK_PATIENT = 1;
	public static final int TO_NEAREST_HOSPITAL = 2;
	public static final int BACK_TO_BASE = 3;

	@Override
	public Behaviour executePlan(int which, Object[] params) {
		switch (which) {
		case PICK_PATIENT:
			return pickPatientPlan(params);
		case TO_NEAREST_HOSPITAL:
			return toNearestHospitalPlan(params);
		case BACK_TO_BASE:
			return backToBase(params);
		}
		return null;
	}

	private Behaviour backToBase(Object[] p) {
		if (p.length != 3)
			return null;
		if (p[0] instanceof AmbulanceAgent && p[1] instanceof AmbulanceDetails){
			if(p[2]!=null){
				return new AmbulanceToBaseBehaviour((AmbulanceAgent) p[0],
						(AmbulanceDetails) p[1],(TrafficUpdateMessage)p[2]);
			}
			else{
				return new AmbulanceToBaseBehaviour((AmbulanceAgent) p[0],
						(AmbulanceDetails) p[1],null);
			}
		}

		return null;
	}

	private Behaviour toNearestHospitalPlan(Object[] p) {
		if (p.length != 4)
			return null;
		if (p[0] instanceof AmbulanceAgent && p[1] instanceof PatientDetails
				&& p[2] instanceof AmbulanceDetails){
			if(p[3] != null)
			return new AmbulanceNearestHospitalBehaviour((AmbulanceAgent) p[0],
					(PatientDetails) p[1], (AmbulanceDetails) p[2], (TrafficUpdateMessage)p[3]);
			else
				return new AmbulanceNearestHospitalBehaviour((AmbulanceAgent) p[0],
						(PatientDetails) p[1], (AmbulanceDetails) p[2], null);
		}

		return null;
	}

	private Behaviour pickPatientPlan(Object[] p) {
		if (p.length != 4)
			return null;
		if (p[0] instanceof AmbulanceAgent
				&& p[1] instanceof AmbulanceNotifyMessage
				&& p[2] instanceof AmbulanceDetails) {
			if (p[3] != null)
				return new PickPatientBehaviour((AmbulanceAgent) p[0],
						(AmbulanceNotifyMessage) p[1], (AmbulanceDetails) p[2],
						(TrafficUpdateMessage) p[3]);
			else
				return new PickPatientBehaviour((AmbulanceAgent) p[0],
						(AmbulanceNotifyMessage) p[1], (AmbulanceDetails) p[2],
						null);
		}

		return null;
	}

	@Override
	public void newGoal(int which, Class behaviour) {
		// TODO Auto-generated method stub

	}

}
