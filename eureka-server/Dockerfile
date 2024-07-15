FROM openjdk:24

ARG JAR_FILE=build/libs/eureka-server-0.0.1.jar
COPY ${JAR_FILE} eureka-server.jar


ENTRYPOINT ["java", "-jar", "/eureka-server.jar"]