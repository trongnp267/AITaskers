# ============================================================
# Dockerfile build backend AITasker - dung khi deploy len
# Render/Railway/Fly (may chu tu build va chay file nay).
# ============================================================

# Buoc 1: build file .jar bang Maven
FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn -q dependency:go-offline
COPY src ./src
RUN mvn -q package -DskipTests

# Buoc 2: chay bang JRE gon nhe
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
