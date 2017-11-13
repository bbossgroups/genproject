#if($enablepinpoint.equals("true"))
java ${vm} $traceagent -jar ${project}-${bboss_version}.jar restart
#else
java ${vm} -jar ${project}-${bboss_version}.jar restart
#end