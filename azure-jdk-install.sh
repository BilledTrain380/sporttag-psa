#!/bin/bash
# author: nmaerchy
# description: Bash script to install a specific openjdk version including openjfx.

url="https://download.oracle.com/otn-pub/java/jdk/8u191-b12/2787e4a523244c269598db4e85c51e0c/jdk-8u191-linux-x64.tar.gz"

echo "OpenJDK download started..."
wget -P $AGENT_TOOLSDIRECTORY $url
echo "OpenJDK download finished!"
