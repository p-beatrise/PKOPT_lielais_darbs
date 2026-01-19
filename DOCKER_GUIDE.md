# Docker Setup Guide

## Quick Start

### 1. Build Docker Image

```bash
# Build the image
docker build -t employee-scheduling:latest .

# Verify it was created
docker images | grep employee-scheduling
```

### 2. Run Docker Container

```bash
# Run in foreground
docker run -p 8081:8081 employee-scheduling:latest

# Run in background (detached)
docker run -d -p 8081:8081 --name emp-scheduler employee-scheduling:latest

# Check running containers
docker ps
```

### 3. Access Application

Open in browser: http://localhost:8081

### 4. Stop Container

```bash
# Stop the container
docker stop emp-scheduler

# Remove the container
docker rm emp-scheduler
```

## Publish to DockerHub

### 1. Login to DockerHub

```bash
docker login
# Enter your username and password
```

### 2. Tag Image with Your Username

```bash
# Replace YOUR_USERNAME with your DockerHub username
docker tag employee-scheduling:latest YOUR_USERNAME/employee-scheduling:latest
docker tag employee-scheduling:latest YOUR_USERNAME/employee-scheduling:1.0
```

### 3. Push to DockerHub

```bash
docker push YOUR_USERNAME/employee-scheduling:latest
docker push YOUR_USERNAME/employee-scheduling:1.0
```

### 4. Verify on DockerHub

Visit: https://hub.docker.com/r/YOUR_USERNAME/employee-scheduling

### 5. Others Can Pull and Run

```bash
docker pull YOUR_USERNAME/employee-scheduling:latest
docker run -p 8081:8081 YOUR_USERNAME/employee-scheduling:latest
```

## Dockerfile Explanation

```dockerfile
# Stage 1: Build
FROM maven:3.9.8-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/quarkus-app/ ./
EXPOSE 8081
CMD ["java", "-jar", "quarkus-run.jar"]
```

**Why multi-stage build?**
- Smaller final image (JRE vs JDK)
- Faster deployment
- Security (no build tools in production)

## Troubleshooting

### Port Already in Use

```bash
# Find process using port 8081
netstat -ano | findstr :8081

# Kill the process (Windows PowerShell)
Stop-Process -Id <PID> -Force

# Or use different port
docker run -p 9090:8081 employee-scheduling:latest
```

### Image Build Fails

```bash
# Clean rebuild
docker build --no-cache -t employee-scheduling:latest .

# Check Docker disk space
docker system df

# Clean up old images
docker system prune -a
```

### Container Crashes

```bash
# View logs
docker logs emp-scheduler

# Run with interactive shell for debugging
docker run -it employee-scheduling:latest /bin/bash
```

## Docker Compose (Optional)

Create `docker-compose.yml`:

```yaml
version: '3.8'
services:
  scheduler:
    build: .
    ports:
      - "8081:8081"
    environment:
      - QUARKUS_HTTP_PORT=8081
```

Run:
```bash
docker-compose up
```

## Image Size Optimization

Current image: ~300MB (with JRE)

To reduce further:
1. Use Alpine base image
2. Remove unnecessary dependencies
3. Use Quarkus native build (GraalVM)
