#! /bin/bash
# (2015) nelson kigen <nellyk89@gmail.com>
# Start JADE and the EQR agents from UNIX

#set -x

#JADE and SRC path
JADE_PATH="/home/nkigen/development/jade/jade"
SRC_PATH="/home/nkigen/development/git/EQR"

#Main EQR package for the agents
AB="org.nkigen.eqr.agents" #Agents package with the execption of the simulation agent

#Classpath for JADE libraries, EQR .class files and its library dependencies
CP=${JADE_PATH}/lib/jade.jar:${SRC_PATH}/bin:${SRC_PATH}/lib/*

#AGENTS CONFIG
NUM_PATIENT_AGENTS=3
NUM_FIRE_AGENTS=1
NUM_HOSPITALS=1
NUM_AMBULANCES=3
NUM_FIREENGINE_AGENTS=1

#Agent Arguements
OSM_FILE_PATH=${SRC_PATH}"/src/trentino.xml"
ROUTER_DIR=${SRC_PATH}"/src/data"
CONFIG_FILE=${SRC_PATH}"/src/config.xml"

#Create all the agents as above including
#ONE router agent
#ONE viewer agent
#ONE updates agent
#ONE EmergencyControlCenterAgent

create_agents(){
PATIENTS=""
for ((i=1; i<=NUM_PATIENT_AGENTS; i++ ));
do
	PATIENTS=${PATIENTS}"patient_"${i}":"${AB}".EQRPatientAgent;"
done
FIRES=""
for ((i=1; i<=NUM_FIRE_AGENTS; i++ ));
do
	FIRES=${FIRES}"fires_"${i}":"${AB}".EQRFireAgent;"
done
FIRE_ENGINES=""
for ((i=1; i<=NUM_FIREENGINE_AGENTS; i++ ));
do
	FIRE_ENGINES=${FIRE_ENGINES}"fire_engine_"${i}":"${AB}".FireEngineAgent;"
done
AMBULANCES=""
for ((i=1; i<=NUM_AMBULANCES; i++ ));
do
	AMBULANCES=${AMBULANCES}"ambulance_"${i}":"${AB}".AmbulanceAgent;"
done
HOSPITALS=""
for ((i=1; i<=NUM_HOSPITALS; i++ ));
do
	HOSPITALS=${HOSPITALS}"hospital_"${i}":"${AB}".HospitalAgent;"
done
ROUTER="router:"${AB}".RoutingAgent;"
UPDATE="updates:"${AB}".EQRUpdatesAgent;"
VIEWER="viewer:"${AB}".EQRViewerAgent;"
export AGENTS="${VIEWER}${UPDATE}${ROUTER}${HOSPITALS}${PATIENTS}${FIRES}${FIRE_ENGINES}${AMBULANCES}simulatorAgent:org.nkigen.eqr.simulation.SimulationAgent;ECC:"${AB}".EmergencyControlCenterAgent"
}

main(){
	create_agents
	ant -f ${SRC_PATH}/build.xml
	java -cp ${CP} jade.Boot -gui -agents sniffer:jade.tools.sniffer.Sniffer ${AGENTS}
}

main
