package org.nkigen.eqr.fires;

import jade.core.behaviours.Behaviour;

import org.nkigen.eqr.agents.EQRFireAgent;
import org.nkigen.eqr.common.EQRGoal;

public class FireGoals extends EQRGoal {
	public static final int REQUEST_FIRE_ENGINE = 0;

	@Override
	public Behaviour executePlan(int which, Object[] params) {
		
		switch(which){
		case REQUEST_FIRE_ENGINE:
			return requestFireEngine(params);
		}
		return null;
	}

	private Behaviour requestFireEngine(Object p[]){
		if(p.length == 2){
			if(p[0] instanceof EQRFireAgent && p[1] instanceof FireDetails)
				 return	 new RequestFireBehaviour((EQRFireAgent)p[0], (FireDetails)p[1]);
		}
		System.out.println("Wrong Number of params ");
		return null;
	}

	@Override
	public void newGoal(int which, Class behaviour) {
		// TODO Auto-generated method stub
		
	}
}
