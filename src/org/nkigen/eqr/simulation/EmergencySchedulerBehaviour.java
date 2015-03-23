package org.nkigen.eqr.simulation;

import java.io.IOException;
import java.util.ArrayList;

import org.nkigen.eqr.common.EQRClock;
import org.nkigen.eqr.common.EmergencyDetails;
import org.nkigen.eqr.common.EmergencyStatus;
import org.nkigen.eqr.fires.FireDetails;
import org.nkigen.eqr.logs.EQRLogger;
import org.nkigen.eqr.messages.ChangeEmergencyStatusMessage;
import org.nkigen.eqr.messages.EmergencyScheduleMessage;
import org.nkigen.eqr.messages.FireInitMessage;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.util.Logger;

/**
 * Schedules the Emergencies(Fire and Patients) Period of 1 min simulation time
 * = real_time*rate
 * 
 * @author nkigen
 *
 */
public class EmergencySchedulerBehaviour extends TickerBehaviour {

	public static final long REAL_TIME_PERIOD = 1000;
	ArrayList<AID> agents;
	ArrayList<Long> schedule;
	int type;
	Logger logger;
	int current = 0;
   EQRClock clock = SimulationBehaviour.getClock();
	/**
	 * 
	 * @param a
	 * @param period
	 * @param agents
	 *            Agents to be started
	 * @param schedule
	 *            Schedule on how to start the Agents in agent above(Assumption:
	 *            Schedule is sorted in assending order)
	 */
	public EmergencySchedulerBehaviour(Agent a, int type, long period,
			ArrayList<AID> agents, ArrayList<Long> schedule) {
		super(a, period);
		this.agents = agents;
		this.schedule = schedule;
		this.type = type;
		logger = EQRLogger.prep(logger, myAgent.getLocalName());
	}

	@Override
	protected void onTick() {
		if(clock.currentSimulationTime() > schedule.get(current))
			return;
		if (type == EmergencyScheduleMessage.SCHEDULE_TYPE_FIRES) {
			ChangeEmergencyStatusMessage cesm = new ChangeEmergencyStatusMessage(
					ChangeEmergencyStatusMessage.TYPE_FIRE,
					EmergencyStatus.FIRE_STATUS_ACTIVE);
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.addReceiver(agents.get(current));
			try {
				msg.setContentObject(cesm);
				myAgent.send(msg);
				EQRLogger.log(logger, msg, myAgent.getLocalName(),
						agents.get(current).getLocalName()
								+ " : Has been scheduled");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			current++;
		} else if (type == EmergencyScheduleMessage.SCHEDULE_TYPE_PATIENTS) {
			ChangeEmergencyStatusMessage cesm = new ChangeEmergencyStatusMessage(
					ChangeEmergencyStatusMessage.TYPE_PATIENT,
					EmergencyStatus.PATIENT_WAITING);
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.addReceiver(agents.get(current));
			try {
				msg.setContentObject(cesm);
				myAgent.send(msg);
				EQRLogger.log(logger, msg, myAgent.getLocalName(),
						agents.get(current).getLocalName()
								+ " : Has been scheduled");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			current++;
		}

	}

}
