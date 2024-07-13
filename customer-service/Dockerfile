FROM openjdk:24

ARG JAR_FILE=build/libs/customer-service-0.0.1.jar
COPY ${JAR_FILE} customer-service.jar

ENTRYPOINT ["java", "-jar", "/customer-service.jar"]