#!/bin/bash

# 开始启动后端程序
BASEDIR="./target/purepicks-backend"
CLASSPATH="$BASEDIR/conf/:$BASEDIR/lib/*"
MAIN_MODULE="com.atomcollide.purepicks.purepicksApplication"
LOGFILE="./purepicks-backend_startup.log"

echo "starting $APP_NAME :)"
java -classpath "$CLASSPATH" -Dbasedir="$BASEDIR" -Dfile.encoding="UTF-8" ${MAIN_MODULE} > $LOGFILE 2>&1 &
