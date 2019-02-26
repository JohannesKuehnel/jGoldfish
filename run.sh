#!/bin/bash

deck="$1"
otp="$2"
runs="$3"
turn="$4"

JFLAG="-d bin -sourcepath src"
JC=javac

PACKAGE=at.co.kuehnel.jgoldfish
MAIN=JGoldfish

${JC} ${JFLAG} src/${PACKAGE//\./\/}/*.java
java -cp bin ${PACKAGE}.${MAIN} ${deck} ${otp} ${runs} ${turn}
