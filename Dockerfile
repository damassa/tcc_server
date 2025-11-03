# Etapa 1: build
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

# Etapa 2: runtime
FROM eclipse-temurin:17-jdk-alpine
WORKDIR /app

# Copia apenas o JAR gerado
COPY --from=build /app/target/tcc_server-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta usada pelo Render
ENV PORT=8080
EXPOSE 8080

# Define o comando de inicialização
ENTRYPOINT ["java", "-jar", "app.jar"]
