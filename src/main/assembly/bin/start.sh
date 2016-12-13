#!/bin/sh




LIB_DIR=/data/m2e/lib
LIB_JARS=`ls $LIB_DIR|grep .jar|awk '{print "'$LIB_DIR'/"$0}'|tr "\n" ":"`

nohup java -classpath $LIB_JARS com.github.treasure.m2e.client.OpenReplicatorClient >>m2e.log &