FROM openjdk:11-jre-slim

# Copy and extract artifact
COPY  revcrm-server/build/libs/revcrm-server-1.0.jar /app/

EXPOSE 8800

# https://spring.io/guides/gs/spring-boot-docker/
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app/revcrm-server-1.0.jar"]
