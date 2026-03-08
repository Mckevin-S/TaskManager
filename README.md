# TaskManager - Pipeline CI/CD Complet

Ce projet consiste en la mise en place d'un pipeline CI/CD complet pour une application Spring Boot conteneurisée, répondant aux exigences d'industrialisation de la startup DeployFast.

## 1. Réponses aux Questions Techniques

#### QUESTION 1 — CONCEPTION ARCHITECTURALE ET MODÉLISATION

**1. Reformulation du besoin fonctionnel**
- **Contexte** : Startup DeployFast.
- **Objectif** : Industrialiser une application web REST de gestion de tâches.
- **Acteurs** : Utilisateur authentifié (propriétaire des tâches).
- **Fonctionnalités** : Inscription/Connexion (JWT), CRUD des tâches, Validation, Pagination, Documentation Swagger.
- **Contraintes** : Spring Boot, Docker, MySQL (Prod), H2 (Test), SonarQube, Sécurité DevSecOps.

**2. Modélisation des données**
- **Table `users`** : `id` (PK), `username` (Unique), `password`, `role`.
- **Table `tasks`** : `id` (PK), `title`, `description`, `status` (TODO, IN_PROGRESS, DONE), `user_id` (FK -> users.id).

**3. Structure REST de l'API**
- `POST /api/auth/register` : Création de compte.
- `POST /api/auth/login` : Authentification et obtention du token JWT.
- `GET /api/tasks` : Liste paginée des tâches de l'utilisateur.
- `POST /api/tasks` : Création d'une tâche.
- `PUT /api/tasks/{id}` : Modification.
- `DELETE /api/tasks/{id}` : Suppression.

**4. Architecture applicative**
L'architecture suit les principes **SOLID** et le **Clean Code** :
- `controller` : Interfaces REST.
- `service` : Logique métier.
- `repository` : Accès aux données (Spring Data JPA).
- `model` : Entités et DTOs.
- `security` : Sécurité JWT.

---


#### QUESTION 3 — CONCEPTION DU PIPELINE

**1. Analyse et stratégie CI/CD**
- **Stratégie de branches** : GitHub Flow (branche `main` protégée, développement sur branches de fonctionnalités).
- **Déclencheurs** : `push` sur `main` et `pull_request` vers `main`.

**2. Définition des étapes CI/CD**
1. **Build** : Compilation avec Maven.
2. **Lint** : Vérification du style de code.
3. **Tests unitaires** : Exécution des tests et génération du rapport Jacoco.
4. **Analyse SonarQube** : Analyse qualité et Quality Gate.
5. **Scan de sécurité** : Scan des dépendances (Snyk/Trivy) et scan d'image Docker.
6. **Build Docker** : Création de l'image et push vers Docker Hub.
7. **Déploiement** : Mise à jour automatique via Docker Compose.

**3. Schéma conceptuel**
`Code Push -> GitHub Actions -> Build -> Test -> SonarQube -> Security Scan -> Docker Build -> Docker Hub -> Deploy Server`

---


#### QUESTION 5 — DÉPLOIEMENT AUTOMATIQUE
Le déploiement est automatisé via GitHub Actions. L'image est buildée avec un tag spécifique (commit SHA), envoyée sur un registre, puis le serveur récupère la nouvelle image et relance `docker-compose up -d`.

#### QUESTION 6 — OPTIMISATION & CLEAN CODE

**1. Structuration et Modularité**
Le projet est segmenté en packages clairs. La logique métier est isolée dans les `Services`, garantissant que les `Controllers` restent légers (principe de responsabilité unique).

**2. Optimisation du Pipeline**
- **Mise en cache** : Utilisation de `actions/cache` pour les dépendances Maven afin de réduire le temps de build.
- **Parallélisation** : Les jobs de tests et de scan de sécurité peuvent s'exécuter en parallèle pour accélérer le feedback.

**3. Guide d'exécution**
- **Lancer l'API** : `./mvnw spring-boot:run`
- **Tests** : `./mvnw test`
- **Analyse Sonar** : `./mvnw clean verify sonar:sonar -Dsonar.login=sqp_9aea125d9ae5af1987ad9d6bf536a3c7572799cb`

---

## 2. Guide d'Installation et d'Utilisation

### Prérequis
- Java 17
- Docker & Docker Compose
- Maven

### Installation locale
1. Cloner le dépôt.
2. Configurer la DB (H2 par défaut en local).
3. Lancer : `./mvnw spring-boot:run`.
4. Accéder au Swagger : `http://localhost:8080/swagger-ui/index.html`.

### Dockerisation
```bash
docker-compose up --build
```
Cela lancera l'application et une instance MySQL configurée.
