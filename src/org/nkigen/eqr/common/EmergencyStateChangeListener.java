package org.nkigen.eqr.common;

/**
 * Emergency State Change Listener for Fires and Patients 
 * @author nkigen
 *
 */
public interface EmergencyStateChangeListener {
	public void onEmergencyStateChange(EmergencyDetails ed);
}