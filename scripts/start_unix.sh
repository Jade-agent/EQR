#! /bin/bash
# (2015) nelson kigen <nellyk89@gmail.com>
# Starts the EQR Simulation Platform 

#set -x

JADE_PATH="/home/nkigen/development/jade/jade"
SRC_PATH="/home/nkigen/development/git/EQR"
CP=${JADE_PATH}/lib/jade.jar:${SRC_PATH}/bin:${SRC_PATH}/lib/*
CONFIG_FILE=${SRC_PATH}/src/config.xml

main(){

	ant -f ${SRC_PATH}/build.xml
	java -cp $CP org.nkigen.eqr.StartSimulation $CONFIG_FILE
}
main
