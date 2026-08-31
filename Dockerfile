FROM eclipse-temurin:26-jdk AS build

WORKDIR /workspace

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline

COPY src/ src/
RUN ./mvnw clean package -DskipTests

FROM eclipse-temurin:26-jre

WORKDIR /app

RUN useradd --system --uid 1001 spring
COPY --from=build --chown=spring:spring /workspace/target/*.jar app.jar

USER spring
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
