# Use an official Maven image for building the Java application
FROM maven:3.8-openjdk-11 AS build
WORKDIR /app

# Copy the Maven project files
COPY pom.xml ./
# Copy source directory
COPY src ./src

# Build the application
# We use the build goal for a clean installation of dependencies
RUN mvn clean install

# Use OpenJDK for running the built application
FROM openjdk:11-jre-slim

# Set a non-root user for running the application
RUN addgroup --system appgroup && adduser --system appuser --ingroup appgroup
USER appuser

# Set the working directory and copy the JAR file from the previous build stage
WORKDIR /app
COPY --from=build /app/target/PizzaShop-1.0-SNAPSHOT.jar ./app.jar

# Set the entrypoint to run the application
ENTRYPOINT ["java", "-jar", "./app.jar"]