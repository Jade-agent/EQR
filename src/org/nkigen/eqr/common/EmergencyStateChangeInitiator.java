package org.nkigen.eqr.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class EmergencyStateChangeInitiator implements Serializable {

	 List<EmergencyStateChangeListener> listeners;
	public EmergencyStateChangeInitiator() {
		listeners = new ArrayList<EmergencyStateChangeListener>();
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
