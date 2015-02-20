package org.nkigen.eqr.patients;

import jade.core.behaviours.Behaviour;

import java.util.HashMap;
import java.util.Map;

import org.nkigen.eqr.agents.EQRPatientAgent;

public class PatientGoals {

	public static final int REQUEST_AMBULANCE_PICKUP = 0;
	
	
	Map<Integer, Behaviour> goals;
	
	public PatientGoals(){
		goals = new HashMap<Integer, Behaviour>();
	}
	
	public void initPatientGoals(Map<Integer, Behaviour> goals){
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
}
