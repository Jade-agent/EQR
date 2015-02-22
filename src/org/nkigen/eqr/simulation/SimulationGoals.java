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
    	return null;
    }
	@Override
	public void newGoal(int which, Class behaviour) {
		// TODO Auto-generated method stub
		
	}

}
