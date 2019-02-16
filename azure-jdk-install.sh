#!/bin/bash
# author: nmaerchy
# description: Bash script to install a specific openjdk version including openjfx.

url="https://download.oracle.com/otn-pub/java/jdk/8u201-b09/42970487e3af4f5aa5bca3f542482c60/jdk-8u201-linux-x64.tar.gz"

# Oracle prevents a download without license agreement with a cookie. Workaround: https://stackoverflow.com/questions/10268583/downloading-java-jdk-on-linux-via-wget-is-shown-license-page-instead
echo "OpenJDK download started..."
wget -P $AGENT_TOOLSDIRECTORY --no-cookies --no-check-certificate --header "Cookie: gpw_e24=http%3a%2F%2Fwww.oracle.com%2Ftechnetwork%2Fjava%2Fjavase%2Fdownloads%2Fjdk8-downloads-2133151.html; oraclelicense=accept-securebackup-cookie;" $url
echo "OpenJDK download finished!"
