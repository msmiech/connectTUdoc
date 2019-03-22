FROM maven:3.6.0-jdk-11
LABEL maintainer="ws18_ase_qse_03"
WORKDIR /

COPY pom.xml /pom.xml
RUN mvn verify clean --fail-never

COPY src /src
# Skipping tests for docker because no db available at build time
RUN mvn package -DskipTests
# HTTP port
EXPOSE 9100
# HTTPS port
EXPOSE 8443
CMD ["java", "-agentlib:jdwp=transport=dt_socket,address=8000,server=y,suspend=n", "-jar","target/Backend.jar"]