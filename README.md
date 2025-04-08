# **Backend Medialltech**

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

<img width="252" alt="Capture d’écran 2025-04-08 à 19 55 13" src="https://github.com/user-attachments/assets/0e337472-9f04-4b04-a63b-f3ac6bb964e8" />

Nous allons sélectionner dans l'onglet "SDK" le JDK précédemment téléchargé.

<img width="1006" alt="Capture d’écran 2025-04-04 à 14 13 17" src="https://github.com/user-attachments/assets/c87a290c-04c6-43d6-8582-aab8a66be7b4" />

Vous pouvez laisser le reste par défaut.

3. **Configuration de Gradle**
Dans ton terminal sous ton IDE tu vas renter la commande :
`gradlew build` ou  `./gradlew build`

Ou sur la barre latérale avec le logo Gradle

<img width="361" alt="Capture d’écran 2025-04-07 à 19 32 41" src="https://github.com/user-attachments/assets/6629c762-ae7f-487c-923e-0568f766708c" />

4. **Configuration la base de données**
Vue que nous n'avons pas de serveur de base de données on va devoir créer une nouvelle base de données en localhost pour insérer nos données.
Dans IntelliJ ou votre IDE vous pouvez configurer la base de données en haut à droite

<img width="213" alt="Capture d’écran 2025-04-07 à 16 19 02" src="https://github.com/user-attachments/assets/9bc32da1-91d5-4f07-a94a-0bb0d817c2fe" />


Cliquez sur le + pour ajouter une nouvelle base de données

<img width="278" alt="Capture d’écran 2025-04-07 à 16 19 59" src="https://github.com/user-attachments/assets/ad51723e-8f59-443b-9f34-0a728e3c3a70" />

On sélectionne la source de base de données en Postgre SQL

<img width="490" alt="Capture d’écran 2025-04-07 à 16 21 22" src="https://github.com/user-attachments/assets/2d14bfd4-fd47-4d83-b2d5-b62def365a9e" />

Pour toutes les informations de connexion je t'invite à lire le `application.properties` dans les fichiers du back `src/main/ressources`
<img width="567" alt="Capture d’écran 2025-04-07 à 17 03 33" src="https://github.com/user-attachments/assets/faa5310f-528d-4765-aaa2-35ded964d98b" />


Pour PgAdmin (PostgreSQL), la base de données sera créée automatiquement tu pourras accéder à tes données via PgAdmin ou directement sous ton IntelliJ.

### Lancement

Désormais que tout est mis en place on peut désormais lancer le projet sur IntelliJ, je vais vous montrer comment gagner du temps pour lancer votre application.

Sur IntelliJ, rendez-vous en bas à gauche dans l'onglet 'Services'

<img width="218" alt="Capture d’écran 2025-04-08 à 13 48 54" src="https://github.com/user-attachments/assets/07c1aa05-130a-4326-aa6c-4f083e1608e8" />

Cliquez sur le petit + pour ajouter une configuration :

<img width="202" alt="Capture d’écran 2025-04-08 à 13 49 56" src="https://github.com/user-attachments/assets/89cf455a-4cf4-4a65-9467-8f64593c9ac3" />


Notre projet est en Spring Boot donc nous allons rajouter une configuration Spring Boot

<img width="301" alt="Capture d’écran 2025-04-08 à 13 51 12" src="https://github.com/user-attachments/assets/a7f5cdf0-27d6-46d4-a9e4-98c4058d1e69" />

Normalement IntellIJ rajoutera automatiquement votre point d’entrée pour l'application spring boot.

<img width="452" alt="Capture d’écran 2025-04-08 à 13 54 17" src="https://github.com/user-attachments/assets/5cb4d568-50d8-4816-b473-440d22b3b439" />


Maintenant votre pourrez lancer votre application plus rapidement sur l'onglet Services en bas à gauche, cela aide quand vous avez plusieurs application à lancer.

<img width="195" alt="Capture d’écran 2025-04-08 à 13 55 56" src="https://github.com/user-attachments/assets/289c63c2-9746-42c9-9186-b48fc54368ca" />

