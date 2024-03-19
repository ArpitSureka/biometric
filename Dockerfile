# Use a base image with JDK and Maven installed
FROM maven:3.9.5-openjdk-21 AS builder

# Set the working directory in the container
WORKDIR /app

# Copy the Maven project file to the container
COPY pom.xml .

# Download dependencies and cache them in Docker layers
RUN mvn dependency:go-offline

# Copy the entire project source
COPY src ./src

# Build the application
RUN mvn package -DskipTests

# Use a smaller base image for the runtime environment
FROM adoptopenjdk/openjdk21:jre-21.0.2_9-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the JAR file from the builder stage to the runtime image
COPY --from=builder /app/target/*.jar ./app.jar

# Expose the port the app runs on
EXPOSE 8080

# Specify the command to run the service
CMD ["java", "-jar", "app.jar"]
