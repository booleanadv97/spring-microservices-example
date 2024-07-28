FROM openjdk:24

ARG JAR_FILE=build/libs/keycloak-0.0.1.jar
COPY ${JAR_FILE} keycloak.jar


ENTRYPOINT ["java", "-jar", "/keycloak.jar"]