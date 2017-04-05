#if($enablepinpoint.equals("true"))
set "AGENT_VERSION=$AGENT_VERSION"
set "AGENT_ID=$AGENT_ID"
set "APPLICATION_NAME=$APPLICATION_NAME"
set "AGENT_PATH=$AGENT_PATH"
java -Xms512m -Xmx512m -Xmn256m -XX:PermSize=256M -XX:MaxPermSize=256M -javaagent:%AGENT_PATH%/pinpoint-bootstrap-1.6.0.jar -Dpinpoint.agentId=%AGENT_ID% -Dpinpoint.applicationName=%APPLICATION_NAME% -jar bboss-rt-${bboss_version}.jar 
#else
java -Xms1024m -Xmx1024m -Xmn512m -XX:PermSize=128M -XX:MaxPermSize=256M -jar bboss-rt-${bboss_version}.jar 
#end