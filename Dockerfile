FROM openjdk:17-alpine
VOLUME /tmp
COPY target/drone-service-0.0.1-SNAPSHOT.jar drone-service.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "drone-service.jar"]