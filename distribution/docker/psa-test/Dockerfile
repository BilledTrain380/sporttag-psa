FROM openjdk:8-jdk-alpine
LABEL maintainer="BiledTrain380@gamil.com"

ARG PSA_VERSION

RUN mkdir -p /opt/psa/db

EXPOSE 8080/tcp

# PSA jar file will be copied to this directory before build
COPY *.jar /opt/psa/

WORKDIR /opt/psa/

ENV server_port 8080
ENV spring_profiles_active prod,data
ENV psa_version $PSA_VERSION

ENTRYPOINT ["sh", "-c", "java -jar PSA-${psa_version}.jar"]
