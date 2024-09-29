FROM openjdk:24

ARG JAR_FILE=build/libs/order-service-0.0.1.jar
COPY ${JAR_FILE} order-service.jar


ENTRYPOINT ["java", "-jar", "/order-service.jar"]