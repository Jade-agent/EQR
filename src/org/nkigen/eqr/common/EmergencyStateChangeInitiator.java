package org.nkigen.eqr.common;

import java.util.ArrayList;
import java.util.List;

public class EmergencyStateChangeInitiator {

	 List<EmergencyStateChangeListener> listeners;
	private static EmergencyStateChangeInitiator initiator;
	protected EmergencyStateChangeInitiator() {
		listeners = new ArrayList<EmergencyStateChangeListener>();
	}
	public static EmergencyStateChangeInitiator getInstance(){
		if(initiator == null)
			initiator = new EmergencyStateChangeInitiator();
		return initiator;
	}

	public void addListener(EmergencyStateChangeListener listener) {
		listeners.add(listener);
	}
	
	public void notifyStateChanged(EmergencyDetails ed){
		System.out.println(getClass().getName()+" notified"+ " "+ listeners.size());
		for(EmergencyStateChangeListener l : listeners)
			l.onEmergencyStateChange(ed);
	}
}
