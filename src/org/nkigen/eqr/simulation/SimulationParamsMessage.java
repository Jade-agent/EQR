package org.nkigen.eqr.simulation;

import jade.core.AID;

import java.io.Serializable;
import java.util.ArrayList;

import org.nkigen.eqr.common.EmergencyResponseBase;
import org.nkigen.maps.routing.EQRPoint;

public class SimulationParamsMessage implements Serializable {

	ArrayList<EQRPoint> patients;
	ArrayList<EQRPoint> fires;
	ArrayList<EmergencyResponseBase> ambulances;
	ArrayList<EmergencyResponseBase> fire_engines;
	ArrayList<EmergencyResponseBase> hospitals;
	/*Simulation parameters*/
	double rate;
	
	/*Routing files*/
	String routing_config_file;
	String routing_data_dir;
	
	public SimulationParamsMessage() {
	patients = new ArrayList<EQRPoint>();
	fires = new ArrayList<EQRPoint>();
	ambulances = new ArrayList<EmergencyResponseBase>();
	fire_engines = new ArrayList<EmergencyResponseBase>();
	hospitals = new ArrayList<EmergencyResponseBase>();
	}

	public ArrayList<EQRPoint> getPatients() {
		return patients;
	}

	public void setPatients(ArrayList<EQRPoint> patients) {
		this.patients = patients;
	}

	public ArrayList<EQRPoint> getFires() {
		return fires;
	}

	public void setFires(ArrayList<EQRPoint> fires) {
		this.fires = fires;
	}

	public ArrayList<EmergencyResponseBase> getAmbulances() {
		return ambulances;
	}

	public void setAmbulances(ArrayList<EmergencyResponseBase> ambulances) {
		this.ambulances = ambulances;
	}

	public ArrayList<EmergencyResponseBase> getFire_engines() {
		return fire_engines;
	}

	public void setFire_engines(ArrayList<EmergencyResponseBase> fire_engines) {
		this.fire_engines = fire_engines;
	}

	public ArrayList<EmergencyResponseBase> getHospitals() {
		return hospitals;
	}

	public void setHospitals(ArrayList<EmergencyResponseBase> hospitals) {
		this.hospitals = hospitals;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}

	public String getRouting_config_file() {
		return routing_config_file;
	}

	public void setRouting_config_file(String routing_config_file) {
		this.routing_config_file = routing_config_file;
	}

	public String getRouting_data_dir() {
		return routing_data_dir;
	}

	public void setRouting_data_dir(String routing_data_dir) {
		this.routing_data_dir = routing_data_dir;
	}
}
