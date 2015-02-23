#! /bin/bash

# (2015) nelson kigen <nellyk89@gmail.com>
# Start JADE and the EQR agents from UNIX

#set -x

#JADE and SRC path
JADE_PATH="/home/nkigen/development/jade/jade"
SRC_PATH="/home/nkigen/development/git/EQR"

#Main package for the agents
AB="org.nkigen.eqr.agents" #Agents package with the execption of the simulation agent

#AGENTS CONFIG
NUM_PATIENT_AGENTS=2
NUM_FIRE_AGENTS=2
NUM_HOSPITALS=1
NUM_AMBULANCES=2
NUM_FIREENGINES=2


#Create all the agents as above including
#ONE router agent
#ONE viewer agent
#ONE updates agent
#ONE EmergencyControlCenterAgent
create_agents(){
#Create Patient Agents
PATIENTS=""
for ((i=0; i<=NUM_PATIENT_AGENTS; i++ ));
do
	PATIENTS=${PATIENTS}"patient_"${i}":"${AB}".EQRPatientAgent;"
done

FIRES=""
for ((i=0; i<=NUM_FIRE_AGENTS; i++ ));
do
	FIRES=${FIRES}"fires_"${i}":"${AB}".EQRFireAgent;"
done
#echo $FIRES
#echo $PATIENTS
export AGENTS="${PATIENTS}${FIRES}simulatorAgent:org.nkigen.eqr.simulation.SimulationAgent;ECC:"${AB}".EmergencyControlCenterAgent"

}

main(){
	create_agents
	ant -f ${SRC_PATH}/build.xml
	java -cp ${JADE_PATH}/lib/jade.jar:${SRC_PATH}/bin jade.Boot -gui -agents ${AGENTS} 

}

main
