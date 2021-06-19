#!/usr/bin/env bash

ID=`ps -ef | grep "Jupiter-example" | grep -v "grep" |grep -v "startup"| awk '{print $2}'`
for id in $ID
do
kill -9 $id
echo "killed $id"
done

nohup java -jar -Dlogback.configurationFile=/Users/chenjie/Documents/dev/github/Jupiter/Jupiter-example/src/main/resources/logback.xml Jupiter-example.jar > log.log 2>&1 &