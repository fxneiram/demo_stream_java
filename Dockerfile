# Usa una imagen base de Java
FROM openjdk:17-jdk-slim

# Instala FFmpeg
RUN apt-get update && apt-get install -y ffmpeg && apt-get clean && rm -rf /var/lib/apt/lists/*

# Establece el directorio de trabajo
WORKDIR /app

# Copia el archivo JAR de tu aplicación
COPY target/stream-0.0.1-SNAPSHOT.war app.war

# Expone el puerto de la aplicación
EXPOSE 8082

# Comando para ejecutar la aplicación
CMD ["java", "-jar", "app.war"]
