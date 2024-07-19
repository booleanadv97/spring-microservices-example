FROM openjdk:24

ARG JAR_FILE=build/libs/gateway-0.0.1.jar
COPY ${JAR_FILE} gateway.jar


ENTRYPOINT ["java", "-jar", "/gateway.jar"]