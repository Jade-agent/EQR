package org.nkigen.eqr.simulation;

import jade.core.behaviours.Behaviour;

import org.nkigen.eqr.common.EQRGoal;

public class SimulationGoals extends EQRGoal {

	public static final int INIT_SIMULATION = 0;
	@Override
	public Behaviour executePlan(int which, Object[] params) {
	
		switch(which){
		case INIT_SIMULATION:
			return initSimulationGoal(params);
		}
		return null;
	}

    private Behaviour initSimulationGoal(Object[] p){
    	if(p.length != 2)
    		return null;
    	else{
    		if(p[0] instanceof SimulationAgent && p[1] instanceof String){
    			return new InitSimulationBehaviour((SimulationAgent)p[0], (String)p[1]);
    		}
    	}
    	return null;
    }
	@Override
	public void newGoal(int which, Class behaviour) {
		// TODO Auto-generated method stub
		
	}

}
