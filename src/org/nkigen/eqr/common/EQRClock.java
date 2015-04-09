package org.nkigen.eqr.common;

import java.io.Serializable;

/**
 * A clock abstraction (One for each Agent)
 * 
 * @author nkigen
 *
 */
public class EQRClock implements Serializable {
	/**
	 * Start Time of the Agent
	 */
	long start;
	/**
	 * Simulation Rate;
	 */
	double rate;

	/**
	 * 
	 * @param rate Simulation rate
	 */
	public EQRClock(double rate) {
		start = System.currentTimeMillis();
		this.rate = rate;
	}

	public double getRate(){
		return rate;
	}
	/**
	 * Converts Simulation Time to Real Time
	 * @param time current simulation time
	 * @return
	 */
	public long simulToReal(long time) {
		return (long) (time / rate);
	}

	/**
	 * Converts real time to simulation time
	 * @param time Current Real Time
	 * @return
	 */
	public long RealToSimul(long time) {
		return (long) (time * rate);
	}

	/**
	 * 
	 * @return current simulation time
	 */
	public long currentSimulationTime() {
		return (long) ((System.currentTimeMillis() - start) * rate);
	}

	/**
	 * 
	 * @return Current Real Time
	 */
	public long currentRealTime() {
		return (long) (System.currentTimeMillis() - start);
	}

}
