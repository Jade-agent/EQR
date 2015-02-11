package org.nkigen.eqr.models;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

import desmoj.core.dist.ContDistExponential;
import desmoj.core.dist.ContDistUniform;
import desmoj.core.simulator.Experiment;
import desmoj.core.simulator.Model;
import desmoj.core.simulator.ProcessQueue;
import desmoj.core.simulator.TimeInstant;
import desmoj.core.simulator.TimeSpan;
import desmoj.core.statistic.Histogram;
import desmoj.core.statistic.TimeSeries;
import desmoj.core.util.AccessPoint;
import desmoj.core.util.Parameterizable;
import desmoj.extensions.experimentation.reflect.MutableFieldAccessPoint;

public class EmergencyArrivalModel extends Model implements Parameterizable {

	public int numVehicles = 10;
	public int attended_emergencies=0;
	public int arrivedPerson = 0;
	ContDistExponential arrival_time; //Emergency Arrivals using an exponential Model
	ContDistUniform response_time; //Response Time for the Emergency Workers
	public ProcessQueue<EmergencyVehicle> emergency_vehicles;
	public ProcessQueue<EmergencyPerson> emergency_person;
	
	protected TimeSeries emergenciesServed;
	protected TimeSeries vehiclesDispatched;  
	protected Histogram waitTimeHistogram;
	
	public EmergencyArrivalModel(Model arg0, String arg1, boolean arg2,
			boolean arg3) {
		super(arg0, arg1, arg2, arg3);
		// TODO Auto-generated constructor stub
	}
	
	
	public EmergencyArrivalModel(){
		this(null,"EmergencyArrivalModel",true,true);
	}

	public double getArrival_time() {
		return arrival_time.sample();
	}

	public double getResponse_time() {
		return response_time.sample();
	}
	@Override
	public String description() {
		return "This model simulates the arrival of Emergencies";
	}

	@Override
	public void doInitialSchedules() {

		for (int i = 0; i < numVehicles; i++) {
		    EmergencyVehicle vancarrier = new EmergencyVehicle(this, "Emergency Vehicle", true);
			vancarrier.activate();
		}

		//create a truck spring
		EmergencyGenerator firstarrival =
			new EmergencyGenerator(this, "EmergencyArrival", false);

		firstarrival.schedule(new TimeSpan(getArrival_time()));
		
	}

	@Override
	public void init() {
		

        emergenciesServed = new TimeSeries(this, "EmergenciesServed", new TimeInstant(0), new TimeInstant(1500), true, false);
        vehiclesDispatched = new TimeSeries(this, "Vehicles Dispatches", new TimeInstant(0), new TimeInstant(1500), true, false);
        waitTimeHistogram = new Histogram(this, "Emergencies Wait Times", 0, 16, 10, true, false);
        
	  arrival_time = new ContDistExponential(this, "EmergencyArrivalTime", 2.0 ,true, false);
	  response_time = new ContDistUniform(this, "EmergencyArrivalTime", 4.0, 7.0 ,true, false);
	  emergency_person = new ProcessQueue<EmergencyPerson>(this, "Emergency Queue", true, true);
	  emergency_vehicles = new ProcessQueue<EmergencyVehicle>(this, "Emergency Vehicles Queue", true, true);
			  
		
	}
	
	
	@Override
	public Map<String, AccessPoint> createParameters() {
		Map<String, AccessPoint> pm = new TreeMap<String, AccessPoint>();
		pm.put("EmergencyVehiclesNumber", new MutableFieldAccessPoint("EmergencyVehiclesNumber", this));
		return pm;
	}
	

}
