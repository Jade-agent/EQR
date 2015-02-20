package org.nkigen.eqr.common;

import jade.core.behaviours.Behaviour;

/**
 * Base class for the Goals of all the agents in the system
 * @author nkigen
 *
 */
public abstract class EQRGoal {

	
	public abstract Behaviour executePlan(int which, Object params[]);
	
}