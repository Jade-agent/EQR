package org.nkigen.eqr.models;

import desmoj.core.simulator.Model;
import desmoj.core.simulator.SimProcess;
import desmoj.core.simulator.TimeInstant;
import desmoj.core.simulator.TimeOperations;
import desmoj.core.simulator.TimeSpan;

public class EmergencyVehicle extends SimProcess {

	EmergencyArrivalModel model;
	
	public EmergencyVehicle(Model arg0, String arg1, boolean arg2) {
		super(arg0, arg1, arg2);
		model = (EmergencyArrivalModel)arg0;
	}

	@Override
	public void lifeCycle() {
		while(true){
			if(model.emergency_person.isEmpty()){
				model.emergency_vehicles.insert(this);
				passivate();
			}
			else{
				System.out.println("Emergency Vehicle");
				EmergencyPerson person = model.emergency_person.first();
				model.emergency_person.remove(person);
				person.endWait();
				hold(new TimeSpan(model.getResponse_time()));
				EmergencyHandler.getInstance().newEmergency();
				person.activate(new TimeSpan(0.0));
			}
		}
		
	}


}
