package org.nkigen.eqr.common;

import java.io.Serializable;

import jade.core.behaviours.Behaviour;

/**
 * Base class for the Goals of all the agents in the system
 * @author nkigen
 *
 */
public abstract class EQRGoal implements Serializable {

	
	public abstract Behaviour executePlan(int which, Object params[]);
	public abstract void newGoal(int which, Class behaviour);
	
}