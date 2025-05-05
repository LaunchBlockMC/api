# Use a more complete and performant JDK image for the build stage
FROM eclipse-temurin:19-jdk-jammy as build

# Install necessary utilities
RUN apt-get update && apt-get install -y bash curl

# Set the working directory
WORKDIR /app

# Copy Gradle wrapper and configuration files first for better caching
COPY gradlew .
COPY gradle/wrapper/ gradle/wrapper/
COPY build.gradle .
COPY settings.gradle .
COPY gradle.properties .

# Ensure the gradlew script is executable
RUN chmod +x gradlew

# Copy the source code
COPY ./src ./src

# Build the application
RUN ./gradlew build --no-daemon

# Use a slim runtime image
FROM eclipse-temurin:19-jdk-jammy as runtime

ENV JAVA_OPTS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager --enable-preview"

WORKDIR /app

COPY --from=build /app/build/quarkus-app/lib/ /app/lib/
COPY --from=build /app/build/quarkus-app/*.jar /app/
COPY --from=build /app/build/quarkus-app/app/ /app/app/
COPY --from=build /app/build/quarkus-app/quarkus/ /app/quarkus/

RUN apt-get update && apt-get install -y curl

HEALTHCHECK --interval=10s --timeout=3s --retries=20 \
  CMD curl --silent --fail http://localhost:8080/q/health || exit 1

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/quarkus-run.jar"]
