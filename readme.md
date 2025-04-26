
### **Description**
Ce projet est une application backend construite avec **Spring Boot**. Il offre des services RESTful pour l'utilisation de medialltech et de sa base de données.

### Pré-requis
Téléchargement de [Java 21](oracle via ce lien : https://www.oracle.com/fr/java/technologies/downloads/)
Téléchargement [PostgreSQL](https://www.postgresql.org/download/windows/) version la plus récente
Téléchargement d'un IDE de préférence [IntelliJ IDEA](https://www.jetbrains.com/idea/download/?section=windows)

## Installation

1. **Cloner le repository**  
   Clonez ce projet sur votre machine locale en utilisant la commande suivante sur votre terminal IntelliJ :
   `git clone https://github.com/OraneMertz/medialltechv2.git`


2. **Configuration du JDK**

Pour configurer le bon JDK sur le proje, il faut se rendre en haut à droite sur l'engrenage pour accéder au "Project Structure"

<img src="ressources/img/Capture%20d’écran%202025-04-04%20à%2014.10.13.png">

Nous allons sélectionner dans l'onglet "SDK" le JDK précédemment téléchargé.

![[Capture d’écran 2025-04-08 à 13.40.33.png]]

Vous pouvez laisser le reste par défaut.

3. **Configuration de Gradle**
   Dans ton terminal sous ton IDE tu vas renter la commande :
   `gradlew build` ou  `./gradlew build`

Ou sur la barre latérale avec le logo Gradle

![[Capture d’écran 2025-04-07 à 19.32.41.png]]

4. **Configuration la base de données**
   Vue que nous n'avons pas de serveur de base de données on va devoir créer une nouvelle base de données en localhost pour insérer nos données.
   Dans IntelliJ ou votre IDE vous pouvez configurer la base de données en haut à droite

![[Capture d’écran 2025-04-07 à 16.19.02.png]]


Cliquez sur le + pour ajouter une nouvelle base de données
![[Capture d’écran 2025-04-07 à 16.19.59.png]]

On sélectionne la source de base de données en Postgre SQL

![[Capture d’écran 2025-04-07 à 16.21.22.png]]

Pour toutes les informations de connexion je t'invite à lire le `application.properties`

![[Capture d’écran 2025-04-07 à 17.06.25.png]]

Pour PgAdmin (PostgreSQL), la base de données sera créée automatiquement tu pourras accéder à tes données via PgAdmin ou directement sous ton IntelliJ.

### Lancement

Désormais que tout est mis en place on peut désormais lancer le projet sur IntelliJ, je vais vous montrer comment gagner du temps pour lancer votre application.

Sur IntelliJ, rendez-vous en bas à gauche dans l'onglet 'Services'![[Capture d’écran 2025-04-08 à 13.48.54.png]]

Cliquez sur le petit + pour ajouter une configuration :
![[Capture d’écran 2025-04-08 à 13.49.56.png]]

Notre projet est en Spring Boot donc nous allons rajouter une configuration Spring Boot

![[Capture d’écran 2025-04-08 à 13.51.12.png]]

Normalement IntellIJ rajoutera automatiquement votre point d’entrée pour l'application spring boot.

![[Capture d’écran 2025-04-08 à 13.54.17.png]]

Maintenant votre pourrez lancer votre application plus rapidement sur l'onglet Services en bas à gauche, cela aide quand vous avez plusieurs application à lancer.
![[Capture d’écran 2025-04-08 à 13.55.56.png]]
