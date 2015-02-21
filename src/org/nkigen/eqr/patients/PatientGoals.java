package org.nkigen.eqr.patients;

import jade.core.behaviours.Behaviour;
import org.nkigen.eqr.common.EQRGoal;

import java.util.HashMap;
import java.util.Map;

import org.nkigen.eqr.agents.EQRPatientAgent;

public class PatientGoals extends EQRGoal {

	public static final int REQUEST_AMBULANCE_PICKUP = 1;
	
	
	Map<Integer, Class> goals;
	
	public PatientGoals(){
		goals = new HashMap<Integer, Class>();
	}
	
	public void initPatientGoals(Map<Integer, Class> goals){
		this.goals = goals;
	}
	
	public Behaviour executePlan(int which, Object params[]){
		switch(which){
		case REQUEST_AMBULANCE_PICKUP:
			return ambulancePickupRequest(params);
			default:
				System.out.println("Plan not available to achieve goal "+which);
				return null;
		}
	}
	
	private Behaviour ambulancePickupRequest(Object p[]){
		if(p.length == 2){
			if(p[0] instanceof EQRPatientAgent && p[1] instanceof PatientDetails)
				 return	 new RequestAmbulanceBehaviour((EQRPatientAgent)p[0], (PatientDetails)p[1]);
		}
		System.out.println("Wrong Number of params ");
		return null;
	}

	@Override
	public void newGoal(int which, Class behaviour) {
		goals.put(which, behaviour);
		
	}
}
