FROM openjdk:24

ARG JAR_FILE=build/libs/cart-service-0.0.1.jar
COPY ${JAR_FILE} cart-service.jar

ENTRYPOINT ["java", "-jar", "/cart-service.jar"]