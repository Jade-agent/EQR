package org.nkigen.eqr.fireengine;

import jade.core.behaviours.Behaviour;

import org.nkigen.eqr.agents.FireEngineAgent;
import org.nkigen.eqr.common.EQRGoal;
import org.nkigen.eqr.messages.FireEngineRequestMessage;
import org.nkigen.eqr.messages.TrafficUpdateMessage;

public class FireEngineGoals extends EQRGoal {

	public static final int ATTEND_TO_FIRE = 1;
	public static final int BACK_TO_BASE = 2;

	@Override
	public Behaviour executePlan(int which, Object[] params) {
		switch (which) {
		case ATTEND_TO_FIRE:
			return attendToFirePlan(params);
		case BACK_TO_BASE:
			return backToBasePlan(params);
		}
		return null;
	}

	private Behaviour attendToFirePlan(Object[] p) {
		if (p.length != 4)
			return null;
		if (p[0] instanceof FireEngineAgent
				&& p[1] instanceof FireEngineRequestMessage
				&& p[2] instanceof FireEngineDetails) {
			if (p[3] != null) {
				return new AttendToFireBehaviour((FireEngineAgent) p[0],
						(FireEngineRequestMessage) p[1],
						(FireEngineDetails) p[2], (TrafficUpdateMessage) p[3]);
			}

			else {
				return new AttendToFireBehaviour((FireEngineAgent) p[0],
						(FireEngineRequestMessage) p[1],
						(FireEngineDetails) p[2], null);
			}
		}
		return null;
	}

	private Behaviour backToBasePlan(Object[] p) {
		if (p.length != 3)
			return null;
		if (p[0] instanceof FireEngineAgent
				&& p[1] instanceof FireEngineDetails) {
			if (p[2] != null) {
				return new EngineBackToBase((FireEngineAgent) p[0],
						(FireEngineDetails) p[1], (TrafficUpdateMessage) p[2]);
			} else {
				return new EngineBackToBase((FireEngineAgent) p[0],
						(FireEngineDetails) p[1], null);
			}

		}
		return null;
	}

	@Override
	public void newGoal(int which, Class behaviour) {
		// TODO Auto-generated method stub

	}

}
