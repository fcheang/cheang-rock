echo remove war
rm "C:\Program Files\Apache Software Foundation\Tomcat 5.5\webapps\scheduler.war"
echo remove webapp directory
rm -rf "C:\Program Files\Apache Software Foundation\Tomcat 5.5\webapps\scheduler"
rm -rf "C:\Program Files\Apache Software Foundation\Tomcat 5.5\work\Catalina"
echo copy war
cp scheduler.war "C:\Program Files\Apache Software Foundation\Tomcat 5.5\webapps"
echo done
