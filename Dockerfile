FROM openjdk:21-jdk-slim

# Créer un utilisateur non-root pour la sécurité
RUN addgroup --system spring && adduser --system spring --ingroup spring
USER spring:spring

# Copier le jar
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# Exposer le port (Koyeb utilise la variable PORT)
EXPOSE 8080

# Commande de démarrage
ENTRYPOINT ["java", "-jar", "/app.jar"]