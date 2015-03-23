package org.nkigen.eqr.simulation;

import java.util.ArrayList;
import java.util.List;

import jade.core.AID;
import jade.core.behaviours.Behaviour;

import org.nkigen.eqr.common.EQRGoal;

public class SimulationGoals extends EQRGoal {

	public static final int INIT_SIMULATION = 0;
	public static final int SCHEDULE_EMERGENCY = 1;
	public static final int TRAFFIC_UPDATES = 2;

	@Override
	public Behaviour executePlan(int which, Object[] params) {

		switch (which) {
		case INIT_SIMULATION:
			return initSimulationGoal(params);
		case TRAFFIC_UPDATES:
			return trafficUpdateGoal(params);
		case SCHEDULE_EMERGENCY:
			return scheduleEmergencyGoal(params);
		}
		return null;
	}

	private Behaviour scheduleEmergencyGoal(Object[] p) {
		if (p.length != 5)
			return null;
		else {
			if (p[0] instanceof SimulationAgent && p[1] instanceof Integer
					&& p[2] instanceof Long && p[3] instanceof List<?>
					&& p[4] instanceof List<?>) {
				return new EmergencySchedulerBehaviour((SimulationAgent) p[0],
						(int) p[1], (long) p[2], (ArrayList<AID>) p[3],
						(ArrayList<Long>) p[4]);
			}
		}
		return null;
	}

	private Behaviour initSimulationGoal(Object[] p) {
		if (p.length != 2)
			return null;
		else {
			if (p[0] instanceof SimulationAgent && p[1] instanceof String) {
				return new InitSimulationBehaviour((SimulationAgent) p[0],
						(String) p[1]);
			}
		}
		return null;
	}

	private Behaviour trafficUpdateGoal(Object[] p) {
		if (p.length != 3)
			return null;
		else {
			if (p[0] instanceof SimulationAgent && p[1] instanceof Long && p[2] instanceof Long) {
				return new TrafficUpdatesBehaviour((SimulationAgent) p[0],(long)p[1],(long)p[2]);
			}
		}
		return null;
	}

	@Override
	public void newGoal(int which, Class behaviour) {
		// TODO Auto-generated method stub

	}

}
