package org.nkigen.eqr.models;

import desmoj.core.simulator.ExternalEvent;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.TimeSpan;

public class EmergencyGenerator extends ExternalEvent {

	EmergencyArrivalModel model;
	public EmergencyGenerator(Model arg0, String arg1, boolean arg2) {
		super(arg0, arg1, arg2);
		model = (EmergencyArrivalModel)arg0;
	}

	@Override
	public void eventRoutine() {
		System.out.println("Truck gen");
		EmergencyPerson person = new EmergencyPerson(model, "EmergencyPerson", true);
		person.activate();
		this.schedule(new TimeSpan(model.getArrival_time()));
		model.emergenciesServed.update(++model.attended_emergencies);
		
	}

}
