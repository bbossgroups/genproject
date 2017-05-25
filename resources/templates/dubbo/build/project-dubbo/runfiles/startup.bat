#if($enablepinpoint.equals("true"))
java -Xms512m -Xmx512m -Xmn256m -XX:PermSize=256M -XX:MaxPermSize=256M $traceagent -jar bboss-rt-${bboss_version}.jar 
#else
java -Xms1024m -Xmx1024m -Xmn512m -XX:PermSize=128M -XX:MaxPermSize=256M -jar bboss-rt-${bboss_version}.jar 
#end