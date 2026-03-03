# ccsirjpahibernate (Java / JPA / Hibernate)

Mini-projet **Java (Maven)** qui illustre :
- un modèle JPA/Hibernate (Client / Assurance / Contrat),
- une couche service CRUD générique (`AbstractFacade` + `IDao`),
- des requêtes **HQL** et **Criteria API**,
- des classes `MainTest...` pour exécuter des scénarios de test (seed + affichage).

## 1) Prérequis

- JDK **21**
- Maven
- MySQL ou MariaDB
- Une base de données nommée **`cc`** (voir configuration plus bas)

> Attention : la configuration actuelle utilise `hbm2ddl.auto=create` (les tables sont recréées).  

## 2) Dépendances Maven

Le projet inclut notamment :
- `hibernate-core 5.6.5.Final`
- `javax.persistence-api 2.2`
- `mysql-connector-java 8.0.33`
- `slf4j-api / slf4j-simple 1.7.36`
- `junit 3.8.1` (scope test)

## 3) Configuration Hibernate

- Fichier : `src/main/resources/hibernate.cfg.xml`
- Chargement : via `HibernateUtil` (`new Configuration().configure().buildSessionFactory()`)

Paramètres présents (à adapter selon la machine) :
- dialect : `org.hibernate.dialect.MariaDB10Dialect`
- driver : `com.mysql.cj.jdbc.Driver`
- url : `jdbc:mysql://localhost:3306/cc?...`
- user : `root`
- `hbm2ddl.auto=create`

### Création DB (exemple)
Créer une base `cc` dans MySQL/MariaDB (via phpMyAdmin, DBeaver, ou SQL).

## 4) Modèle de données

### Entités
- `Client`
  - table : `clients`
  - `cin` unique
  - relation : `Client (1) -> (N) Contrat`
- `Assurance`
  - table : `assurances`
  - `type` = enum `TypeAssurance` (stocké en `STRING`, unique)
  - relation : `Assurance (1) -> (N) Contrat`
- `Contrat`
  - table : `contrats`
  - champs : `dateDebut`, `dateFin` (LocalDate)
  - relations : `ManyToOne` vers `Client` et `Assurance`
  - méthode utilitaire : `isActif(LocalDate reference)` (optionnelle)

Enum :
- `TypeAssurance { AUTO, HABITATION, SANTE, VOYAGE, VIE }`

## 5) Couche service & CRUD

- `IDao` : interface CRUD (`create`, `delete`, `update`, `findById`, `findAll`)
- `AbstractFacade` : implémentation CRUD générique (Session/Transaction)
- Services :
  - `ClientService` : `findByCinHql(String cin)`
  - `AssuranceService` : `findByTypeHql(TypeAssurance)`, `findByTypeCriteria(TypeAssurance)`
  - `ContratService` :
    - `findByClientCinHql(String cin)`, `findByClientCinCriteria(String cin)`
    - `findByAssuranceTypeHql(TypeAssurance)`, `findByAssuranceTypeCriteria(TypeAssurance)`
    - `findActiveAfterHql(LocalDate dateRef)`, `findActiveAfterCriteria(LocalDate dateRef)` (contrats non expirés)

## 6) Exécution

### Build
```bash
mvn clean package
