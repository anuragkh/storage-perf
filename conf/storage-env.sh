#!/usr/bin/env bash

export HADOOP_CONF_DIR="$HADOOP_HOME/conf"
export HADOOP_LIB_DIR="$HADOOP_HOME/lib"
for JAR in $HADOOP_LIB_DIR/*.jar; do
    export CLASSPATH=$JAR:$CLASSPATH
done
