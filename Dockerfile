FROM openjdk:11-jdk as builder
WORKDIR /app
COPY . .
RUN ./gradlew build

FROM openjdk:11-jdk
COPY --from=builder /app/build/libs/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

