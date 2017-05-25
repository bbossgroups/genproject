#!/bin/sh
#if($enablepinpoint.equals("true"))
nohup java -Xms1024m -Xmx1024m -Xmn512m -XX:PermSize=128M -XX:MaxPermSize=256M $traceagent -jar bboss-rt-${bboss_version}.jar > dubbo.log &
#else
nohup java -Xms1024m -Xmx1024m -Xmn512m -XX:PermSize=128M -XX:MaxPermSize=256M -jar bboss-rt-${bboss_version}.jar > dubbo.log & 
#end