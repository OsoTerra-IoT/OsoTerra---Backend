# Container image for hosts without a Java runtime (Render). Railway builds from the pom.xml instead.
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -B -q dependency:resolve
COPY src src
RUN mvn -B -q -DskipTests package

FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
# Render's free instance has 512 MB: a small heap and the serial collector keep the JVM inside it.
ENV JAVA_TOOL_OPTIONS="-Xmx300m -XX:MaxMetaspaceSize=160m -Xss512k -XX:+UseSerialGC -XX:TieredStopAtLevel=1"
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
