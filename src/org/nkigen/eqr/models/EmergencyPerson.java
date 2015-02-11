package org.nkigen.eqr.models;

import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;
import desmoj.core.simulator.TimeInstant;
import desmoj.core.simulator.TimeOperations;

public class EmergencyPerson extends SimProcess {

	EmergencyArrivalModel model;
	private TimeInstant startWait;
	private TimeInstant endWait;
	public EmergencyPerson(Model arg0, String arg1, boolean arg2) {
		super(arg0, arg1, arg2);
		model = (EmergencyArrivalModel)arg0;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void lifeCycle() {
		model.emergency_person.insert(this);
		startWait = presentTime();
		sendTraceNote("EmergencyPersonQueuelength: " + model.emergency_person.length());
		
		if(!model.emergency_vehicles.isEmpty()){
			EmergencyVehicle vehicle = model.emergency_vehicles.first();
			model.emergency_vehicles.remove(vehicle);
			vehicle.activateAfter(this);
			EmergencyHandler.getInstance().newEmergencyVehicle();
			passivate();
		}
		else{
			passivate();
		}
		System.out.println("Person Generated");
		sendTraceNote("Truck was serviced and leaves system.");
		model.emergenciesServed.update(++model.attended_emergencies);
		model.waitTimeHistogram.update(getWaitTime());
		
	}
	public void endWait() {
		endWait = presentTime();
	}
	
	public double getWaitTime() {
		if (startWait != null && endWait != null) 
			return TimeOperations.diff(startWait, endWait).getTimeAsDouble();
		else
			return Double.NaN;
	}

}
