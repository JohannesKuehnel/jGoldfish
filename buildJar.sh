#!/bin/bash
PACKAGE=at.co.kuehnel.jgoldfish
MAIN=JGoldfish
JFLAG="-d bin -sourcepath src"
JC=javac
JAR_NAME=jGoldfish.jar

${JC} ${JFLAG} src/${PACKAGE//\./\/}/*.java

cd bin
jar cfe ../dist/${JAR_NAME} ${PACKAGE}.${MAIN} ${PACKAGE//\./\/}/*.class