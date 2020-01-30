#!/bin/sh

jar_path="./lib"

#---------------------------------#
# Concatanate the classpath       #
#---------------------------------#
for jarfile in "$jar_path"/*.jar ; do
        if [ -z "$JAVA_CLASSPATH" ] ; then
                JAVA_CLASSPATH="$jarfile"
        else   
                JAVA_CLASSPATH="$jarfile:$JAVA_CLASSPATH"
        fi
done

echo $JAVA_CLASSPATH

java -cp $JAVA_CLASSPATH com.company.priceengine.GoogleGeocodeFacade