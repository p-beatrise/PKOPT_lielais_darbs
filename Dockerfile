# Build stage
FROM maven:3.9.8-eclipse-temurin-17 AS build

WORKDIR /app

# Copy pom.xml and download dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the application (skip tests completely - avoid test compilation)
RUN mvn clean package -Dmaven.test.skip=true -Dquarkus.package.type=uber-jar

# Runtime stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy the uber jar from build stage
COPY --from=build /app/target/*-runner.jar /app/app.jar

# Expose the application port
EXPOSE 8081

# Set environment variables
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# Run the application
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar /app/app.jar"]
