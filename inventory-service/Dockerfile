FROM openjdk:24

ARG JAR_FILE=build/libs/inventory-service-0.0.1.jar
COPY ${JAR_FILE} inventory-service.jar


ENTRYPOINT ["java", "-jar", "/inventory-service.jar"]