#! /bin/bash

# (2015) nelson kigen <nellyk89@gmail.com>
# Start the EQR agents from UNIX

JADE_PATH="/home/nkigen/development/jade/jade"
SRC_PATH="/home/nkigen/development/git/EQR/src/org/nkigen"

set -x



main(){
#	java -classpath ${JADE_PATH}/lib/jade.jar jade.Boot -gui
#	javac -classpath lib/jade.jar -d classes ${SRC_PATH}/*.java
ant ${SRC_PATH}

}

main
