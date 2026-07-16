# ============================================================
# Dockerfile deploy CA WEB (frontend + backend) trong MOT service.
# Frontend Next.js duoc xuat ra trang tinh va nhet vao Spring Boot,
# nen chi can mot dia chi duy nhat tren cloud.
# ============================================================

# Buoc 1: build frontend Next.js thanh trang tinh (thu muc out/)
FROM node:20 AS frontend
WORKDIR /fe
COPY frontend/package.json frontend/package-lock.json ./
RUN npm install
COPY frontend/ ./
RUN npm run build

# Buoc 2: build backend, copy trang tinh vao resources/static
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q dependency:go-offline
COPY src ./src
COPY --from=frontend /fe/out ./src/main/resources/static
RUN mvn -q package -DskipTests

# Buoc 3: chay bang JRE gon nhe
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
