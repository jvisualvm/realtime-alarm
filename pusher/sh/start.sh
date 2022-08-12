#!/bin/bash

APP_NAME=pusher.jar
LOG_FILE=pusher.log

pid=`ps -ef|grep $APP_NAME | grep -v grep | awk '{print $2}'`
kill -9 $pid
echo "kill $pid"

sleep 2

if test -e $APP_NAME
then
echo 'start...'


nohup java -jar -server -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=128m -Xmx512m -Xms512m -XX:+PrintGCDetails -Duser.timezone=Asia/Shanghai -Ddruid.mysql.usePingMethod=false $APP_NAME > $LOG_FILE 2>&1 &
tail -f $LOG_FILE

else
echo '$APP_NAME jar not exists'
fi