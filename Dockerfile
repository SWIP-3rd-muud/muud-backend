FROM openjdk:17-ea-33-jdk-buster
ARG JAR_FILE=builds/libs/*.jar
COPY ${JAR_FILE} /muud.jar
ENTRYPOINT ["java", "-jar", "/muud.jar"]