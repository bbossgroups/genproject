#!/bin/sh
java -Xms1024m -Xmx1024m -Xmn512m -XX:PermSize=128M -XX:MaxPermSize=128M -jar ${project}-${bboss_version}.jar  --conf=config-gradle.properties
