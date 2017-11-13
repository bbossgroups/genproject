#!/bin/sh

#if($enablepinpoint.equals("true"))
nohup java ${vm} $traceagent -jar ${project}-${bboss_version}.jar restart --shutdownLevel=C > ${project}.log &
tail -f ${project}.log

#else
nohup java ${vm} -jar ${project}-${bboss_version}.jar restart --shutdownLevel=C > ${project}.log &
tail -f ${project}.log

#end
