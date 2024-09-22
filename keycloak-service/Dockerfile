FROM openjdk:24

ARG JAR_FILE=build/libs/keycloak-service-0.0.1.jar
COPY ${JAR_FILE} keycloak-service.jar


ENTRYPOINT ["java", "-jar", "/keycloak-service.jar"]