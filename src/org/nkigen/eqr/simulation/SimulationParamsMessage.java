package org.nkigen.eqr.simulation;

import jade.core.AID;

import java.io.Serializable;
import java.util.ArrayList;

import org.nkigen.eqr.common.EmergencyResponseBase;
import org.nkigen.maps.routing.EQRPoint;

public class SimulationParamsMessage implements Serializable {

	//If location is set to auto for any of the Emergencies, They are to be generated automatically
	ArrayList<EQRPoint> patients;
	ArrayList<EQRPoint> fires;
	
	/*TODO: Remove this hack with a better fix*/
	int num_patients;
	int num_fires;

	ArrayList<EmergencyResponseBase> ambulances;
	ArrayList<EmergencyResponseBase> fire_engines;
	ArrayList<EmergencyResponseBase> hospitals;
	/*Simulation parameters*/
	double rate;
	long patient_inter_arrival;
	long fire_inter_arrival;
	long traffic_update_period;
	long traffic_max_delay;
	/*Routing files*/
	String routing_config_file;
	String routing_data_dir;
	
	@Override
	public String toString(){
	String text ="";
	text+= "routing file:="+routing_config_file+" routing data dir:="+routing_data_dir+" Simulation RATE:="+rate+" Patient IR:="+patient_inter_arrival+" Fire IR:="+fire_inter_arrival
	+" Traffic Update:="+traffic_update_period+" Traffic max Delay:="+traffic_max_delay;
	return text;
	}
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

	public long getPatient_inter_arrival() {
		return patient_inter_arrival;
	}

	public void setPatient_inter_arrival(long patient_inter_arrival) {
		this.patient_inter_arrival = patient_inter_arrival;
	}

	public long getFire_inter_arrival() {
		return fire_inter_arrival;
	}

	public void setFire_inter_arrival(long fire_inter_arrival) {
		this.fire_inter_arrival = fire_inter_arrival;
	}

	public long getTraffic_update_period() {
		return traffic_update_period;
	}

	public void setTraffic_update_period(long traffic_update_period) {
		this.traffic_update_period = traffic_update_period;
	}

	public long getTraffic_max_delay() {
		return traffic_max_delay;
	}

	public void setTraffic_max_delay(long traffic_max_delay) {
		this.traffic_max_delay = traffic_max_delay;
	}
	public int getNum_patients() {
		return num_patients;
	}
	public void setNum_patients(int num_patients) {
		this.num_patients = num_patients;
	}
	public int getNum_fires() {
		return num_fires;
	}
	public void setNum_fires(int num_fires) {
		this.num_fires = num_fires;
	}
}
