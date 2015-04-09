package org.nkigen.eqr.messages;

import java.io.Serializable;
import java.util.ArrayList;

public class EmergencyScheduleMessage implements Serializable {
	public static final int SCHEDULE_TYPE_PATIENTS = 0;
	public static final int SCHEDULE_TYPE_FIRES = 1;
	int type;
	ArrayList<Long> schedule;
	
	public EmergencyScheduleMessage(int type) {
		this.type = type;
		schedule = new ArrayList<Long>();
	}
	public int getType(){
		return type;
	}
	
	public void setSchedule(double[] sample){
		for(int i=0;i<sample.length;i++)
			schedule.add((long) sample[i]);
	}
	public ArrayList<Long> getSchedule(){
		return schedule;
	}

}
