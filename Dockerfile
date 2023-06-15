FROM openjdk:11-jdk
ARG JAR_FILE=./build/libs/encore-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app.jar
COPY ./src/main/resources/geolite2 src/main/resources/geolite2
ENTRYPOINT ["java","-jar","/app.jar"]
