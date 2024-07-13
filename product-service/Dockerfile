FROM openjdk:24

ARG JAR_FILE=build/libs/product-service-0.0.1.jar
COPY ${JAR_FILE} product-service.jar


ENTRYPOINT ["java", "-jar", "/product-service.jar"]