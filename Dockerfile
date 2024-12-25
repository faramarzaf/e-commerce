# Use OpenJDK 21 as the base image for the build stage
FROM openjdk:21-slim as builder

# Install Maven
RUN apt-get update && \
    apt-get install -y maven && \
    rm -rf /var/lib/apt/lists/*

# Set the working directory in the container
WORKDIR /app

# Copy the pom.xml and src folder to the working directory (for Maven)
COPY pom.xml .
COPY src ./src

# Build the Spring Boot application
RUN mvn clean install -DskipTests

# Verify the build output (this will list the files in /app/target)
RUN ls -l /app/target

# Use a minimal JDK image to run the app
FROM openjdk:21-slim

# Set the working directory in the container
WORKDIR /app

# Copy the .jar file from the builder stage to the runtime container
COPY --from=builder /app/target/E-Commerce-0.0.1-SNAPSHOT.jar /app/e-commerce-app.jar

# Expose the application port (default Spring Boot port is 8080)
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/e-commerce-app.jar"]
