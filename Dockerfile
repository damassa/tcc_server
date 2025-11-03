# Usa imagem base do OpenJDK 17 (a mesma usada no Heroku por padrão)
FROM eclipse-temurin:17-jdk-alpine

# Define o diretório de trabalho
WORKDIR /app

# Copia o arquivo .jar gerado pelo Maven
COPY target/tcc_server-0.0.1-SNAPSHOT.jar app.jar

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Comando de inicialização
ENTRYPOINT ["java", "-jar", "app.jar"]
