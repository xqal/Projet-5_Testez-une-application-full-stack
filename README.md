# NumDev Test Application Full-stack
Ce projet consiste à mettre en place une **stratégie de tests complète** pour une application de gestion de sessions de yoga. L'objectif était d'atteindre une couverture de code **d'au moins 80%** cotés backend et frontend, tout en incluant des **tests unitaires**, **intégration et d'interface (e2e)**

## Prérequis

- **Backend** : Java 11, Spring Boot, JUnit 5
- **Frontend** : Angular, Jest, Cypress
- **Base de données** : MySQL (Developpement), H2 (Tests).


## Installation et lancement de l'application

Git clone :
```bash
git clone https://github.com/xqal/Projet-5_Testez-une-application-full-stack.git
```
Accéder au dossier racine du projet :
```bash
cd Projet-5_Testez-une-application-full-stack
```

### Backend
```bash
mvn clean install
```
Lancer le serveur en base de production Mysql
```bash
mvn spring-boot:run
```
Lancer seerveur e, base de test H2
```bash
mvn spring-boot:run "-Dspring.profiles.active=test"  
```
API est accessible sur : http://localhost:8080

### Frontend
```bash
npm install
npm run start
```
Frontend est accessible sur : http://localhost:4200


## Excécution des Tests et Rapports de Couverture
### Backend (JUnit 5, Mockito, JaCoCo)
Incluent tests unitaires des services et tests d'intégrations des controllers

- Lancer les tests et générer le rapport :
```bash
mvn clean verify
```
**Rapport JaCoCo** : back/target/site/jacoco/index.html

### Frontend (Jest)
Incluent tests Unitaire des services et test d'intégration des composants
- Lancer les tests et générer le rapport :
```bash
npm test -- --coverage
```
**Rapport Jest** : front/coverage/jest/lcov-report/index.html

### End-to-End (Cypress)
- Lancer le backend avec profil test (Base de donnée H2)
```bash
mvn spring-boot:run "-Dspring.profiles.active=test"  
```
- Lancer les tests cypress
```bash
npm run e2e:ci
npm run e2e:coverage  
```
