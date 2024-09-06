# Runtime stage
FROM eclipse-temurin:17-jre-alpine AS runtime

# Set the working directory
WORKDIR /app

# Copy the JAR file from the local machine
COPY product-service/target/*.jar app.jar

# Expose the port on which the application will run
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]

