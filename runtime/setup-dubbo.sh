#!/bin/sh
nohup java -Xms1024m -Xmx1024m -Xmn512m -XX:PermSize=256M -XX:MaxPermSize=512M -jar bboss-rt-${bboss_version}.jar  --conf=config-dubbo.properties > dubbo.log &
tail -f dubbo.log
