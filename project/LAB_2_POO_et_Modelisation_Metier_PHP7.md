# LAB 2 — Programmation Orientée Objet et Modélisation Métier (PHP 7)

Ce laboratoire suit la structure et l’esprit d’un Google Cloud Skills Boost Lab. Il est prêt à l’emploi pour une séance de TP en salle, sans framework, avec PHP 7 strictement.

## Objectifs pédagogiques
- Comprendre et mettre en œuvre les concepts fondamentaux de la POO en PHP 7.
- Concevoir des entités métier avec encapsulation et validation.
- Utiliser la visibilité (public / protected / private), les constructeurs, getters/setters.
- Mettre en place l’héritage et les interfaces.
- Organiser le code avec des namespaces selon une organisation PSR‑4 minimale.
- Manipuler un repository factice pour simuler les opérations CRUD en mémoire.

## Prérequis techniques
- Avoir réalisé le LAB 1 (typages stricts, autoload minimal, exceptions, configuration d’environnement).
- PHP 7.x installé (7.2+ recommandé) et serveur interne PHP disponible.
- Connaissance basique de la ligne de commande (PowerShell/Terminal) et d’un éditeur de code.

## Durée estimée
- 60 à 90 minutes.

## Environnement requis
- OS Windows, macOS ou Linux.
- IDE/éditeur (PhpStorm recommandé) avec accès au projet fourni.

---

## Structure du projet (rappel + nouveautés)
```
project/
  bootstrap.php
  demo_cli.php
  demo_oop.php              # nouveau script de démo POO (CLI)
  public/
    index.php
    oop.php                 # nouvelle page de démo POO (Web)
  src/
    helpers.php
    Exception/
      ValidationException.php
    Service/
      GreetingService.php
    Entity/                 # NOUVEAU
      Etudiant.php
      Filiere.php
    Repository/             # NOUVEAU
      RepositoryInterface.php
      FakeRepository.php
```

---

## Étape 1 — Rappels POO: classe et objet

Explication pédagogique courte:
En POO, une classe définit un plan (attributs et méthodes). Un objet est une instance de cette classe. Nous allons créer deux entités métier simples: Etudiant et Filiere.

Instructions:
1. Ouvrez `project/src/Entity/Filiere.php` et `project/src/Entity/Etudiant.php`.
2. Repérez les éléments suivants:
   - Attributs privés
   - Constructeur
   - Getters et setters
   - Validation dans les setters (ex: id positif)

Extrait de code (Filiere):
```php
<?php
declare(strict_types=1);
namespace App\Entity;
use App\Exception\ValidationException;
class Filiere{private $id; private $libelle; public function __construct(int $id,string $libelle){$this->setId($id);$this->setLibelle($libelle);} public function getId():int{return $this->id;} public function getLibelle():string{return $this->libelle;} public function setId(int $id):void{ if($id<=0){throw new ValidationException("L'identifiant de la filière doit être strictement positif.");} $this->id=$id;} public function setLibelle(string $libelle):void{ $libelle=trim($libelle); if($libelle===''){throw new ValidationException('Le libellé de la filière ne peut pas être vide.');} $this->libelle=$libelle;}}
```

Checkpoint:
- Êtes‑vous capable d’instancier `new Filiere(1, 'Informatique')` sans erreur ?

Astuce pédagogique:
- Les attributs privés garantissent l’encapsulation: l’état interne est modifié uniquement via des méthodes.

Erreur fréquente:
- Oublier `declare(strict_types=1);` en début de fichier, ce qui peut laisser passer des types incorrects.

---

## Étape 2 — Visibilités et validation dans les setters

Explication pédagogique:
La visibilité contrôle l’accès aux membres. Nous appliquons l’encapsulation et validons les entrées via des exceptions.

Instructions:
1. Ouvrez `Etudiant.php`.
2. Observez les validations:
   - `id > 0` et `filiereId > 0`
   - `nom` non vide
   - `email` au format valide (filter_var)

Extrait de code (Etudiant — setters):
```php
public function setEmail(string $email): void {
    $email = trim($email);
    if ($email === '') { throw new ValidationException("L'email ne peut pas être vide."); }
    if (filter_var($email, FILTER_VALIDATE_EMAIL) === false) { throw new ValidationException("Le format de l'email est invalide."); }
    $this->email = $email;
}
```

Checkpoint:
- Modifier l’email d’un étudiant avec `setEmail('x')` doit lever une `ValidationException`.

Astuce pédagogique:
- Centraliser les règles métier dans les setters évite la duplication et assure la cohérence.

Erreur fréquente:
- Ne pas normaliser (trim) les entrées avant validation.

---

## Étape 3 — Interfaces et contrat de repository

Explication pédagogique:
Une interface définit un contrat. Le `RepositoryInterface` formalise les opérations CRUD pour la persistance.

Instructions:
1. Ouvrez `project/src/Repository/RepositoryInterface.php`.
2. Identifiez les méthodes: `create`, `findById`, `findAll`, `update`, `delete`.

Extrait de code:
```php
interface RepositoryInterface {
    public function create($entity);
    public function findById(string $entityClass, int $id);
    public function findAll(string $entityClass): array;
    public function update($entity): void;
    public function delete(string $entityClass, int $id): void;
}
```

Checkpoint:
- Êtes‑vous capable de typer l’argument `int $id` et de comprendre les retours attendus ?

Astuce pédagogique:
- Le typage strict détecte tôt les erreurs de type; PHP 7 ne permet pas les unions de types modernes, on reste simple.

Erreur fréquente:
- Oublier la gestion du cas « entité introuvable » (retourner `null`).

---

## Étape 4 — Implémentation d’un FakeRepository en mémoire

Explication pédagogique:
Pour tester sans base de données, un repository en mémoire est idéal et rapide. Il simule un stockage clé/valeur par classe d’entité.

Instructions:
1. Ouvrez `project/src/Repository/FakeRepository.php`.
2. Comprenez le principe: stockage par FQCN puis par id.
3. Examinez la validation dans `create`, `update`, `delete`.

Extrait de code:
```php
class FakeRepository implements RepositoryInterface {
    private $store = [];
    public function create($entity){ $class = get_class($entity); $id = $this->extractId($entity); if($id<=0){ throw new ValidationException('id positif requis'); } if(!isset($this->store[$class])){ $this->store[$class]=[]; } if(isset($this->store[$class][$id])){ throw new ValidationException('doublon'); } return $this->store[$class][$id]=$entity; }
    public function findById(string $entityClass, int $id){ if($id<=0){ throw new ValidationException('id positif requis'); } return $this->store[$entityClass][$id]??null; }
    public function findAll(string $entityClass): array { return array_values($this->store[$entityClass]??[]); }
    public function update($entity): void { $class=get_class($entity); $id=$this->extractId($entity); if(!isset($this->store[$class][$id])){ throw new ValidationException('introuvable'); } $this->store[$class][$id]=$entity; }
    public function delete(string $entityClass, int $id): void { unset($this->store[$entityClass][$id]); }
    private function extractId($entity): int { if(!method_exists($entity,'getId')){ throw new ValidationException('getId requis'); } return (int)$entity->getId(); }
}
```

Checkpoint:
- Créez une filière et un étudiant, insérez‑les, listez, mettez à jour, supprimez. Les checkpoints affichent l’état attendu.

Astuce pédagogique:
- L’implémentation en mémoire accélère l’apprentissage des concepts métier avant toute I/O persistante.

Erreur fréquente:
- Mettre à jour une entité non insérée auparavant: provoquer une exception contrôlée.

---

## Étape 5 — Démonstration CLI (scénario guidé)

Explication pédagogique:
Un script CLI reproductible permet de vérifier rapidement les concepts et la validation.

Instructions:
- Exécutez depuis la racine du dépôt:
```powershell
php project\demo_oop.php
```

Vérification attendue (extraits):
```
[CHECKPOINT] Filiere créée => "1 - Informatique"
[CHECKPOINT] Etudiant créé => "1 - Alice <alice@example.com> (filiere: 1)"
[CHECKPOINT] Find Etudiant#1 => "1 - Alice <alice@example.com> (filiere: 1)"
[CHECKPOINT] Etudiant mis à jour => "1 - Alice Dupont <alice@example.com> (filiere: 1)"
[CHECKPOINT] Validation attendue (email) => "Le format de l'email est invalide."
[CHECKPOINT] Suppression Etudiant#1 => true
```

Astuce pédagogique:
- Utilisez des valeurs différentes pour tester plusieurs cas limites (ids négatifs, libellé vide, etc.).

Erreur fréquente:
- Oublier d’inclure `bootstrap.php` qui enregistre l’autoload et les helpers (fonction checkpoint).

---

## Étape 6 — Démonstration Web (navigateur)

Explication pédagogique:
La même logique est exposée via une page PHP. Utile pour une démonstration en salle avec un navigateur.

Instructions:
1. Lancez le serveur interne:
```powershell
php -S localhost:8000 -t project\public
```
2. Ouvrez votre navigateur et visitez:
```
http://localhost:8000/oop.php
```

Checkpoint:
- Des paragraphes « CHECKPOINT » s’affichent pour chaque étape du scénario.

Astuce pédagogique:
- Utilisez F12 (console réseau) pour observer que tout se passe côté serveur.

Erreur fréquente:
- Servir un mauvais répertoire avec `-t`. Veillez à utiliser `project\public`.

---

## Étape 7 — Héritage et interfaces (extension optionnelle)

Explication pédagogique:
Vous pouvez créer une interface spécifique `Identifiable` avec `getId(): int` et faire implémenter cette interface par vos entités. Cela renforce le contrat entre entités et repository.

Instructions (optionnel):
- Créez `App\Entity\Identifiable` et implémentez‑la dans `Etudiant` et `Filiere`.
- Adaptez `FakeRepository::extractId` pour vérifier l’instance de `Identifiable`.

Checkpoint:
- Les opérations CRUD doivent continuer à fonctionner inchangées.

Astuce pédagogique:
- Les interfaces minimisent le couplage et facilitent le test.

Erreur fréquente:
- Confondre interface et classe abstraite; ici nous n’avons pas d’attributs, donc une interface est suffisante.

---

## Diagramme de classes (schéma textuel)
```
App\Entity\Filiere
- id: int (private)
- libelle: string (private)
+ __construct(id:int, libelle:string)
+ getId(): int
+ getLibelle(): string
+ setId(id:int): void
+ setLibelle(libelle:string): void

App\Entity\Etudiant
- id: int (private)
- nom: string (private)
- email: string (private)
- filiereId: int (private)
+ __construct(id:int, nom:string, email:string, filiereId:int)
+ getId(): int
+ getNom(): string
+ getEmail(): string
+ getFiliereId(): int
+ setId(id:int): void
+ setNom(nom:string): void
+ setEmail(email:string): void
+ setFiliereId(filiereId:int): void

App\Repository\RepositoryInterface
+ create(entity: object): object
+ findById(entityClass: string, id: int): ?object
+ findAll(entityClass: string): object[]
+ update(entity: object): void
+ delete(entityClass: string, id: int): void

App\Repository\FakeRepository implements RepositoryInterface
- store: array<string, array<int, object>> (private)
```

---

## Résumé final
À l’issue de ce lab, vous avez:
- Modélisé des entités métier avec encapsulation et validation.
- Utilisé des constructeurs, getters/setters, et différentes visibilités.
- Créé et appliqué une interface de repository.
- Implémenté un stockage en mémoire pour tester facilement les opérations CRUD.
- Testé via un script CLI et une page web avec des checkpoints visibles.

## Livrables attendus
- Code PHP conforme à l’organisation par namespaces suivante:
  - `App\Entity` (Etudiant, Filiere)
  - `App\Repository` (RepositoryInterface, FakeRepository)
- Script de démonstration CLI (`project/demo_oop.php`) et page web (`project/public/oop.php`).
- Diagramme de classes (texte) intégré au présent document.
- Projet exécutable avec PHP 7 sans framework.

## Auto‑évaluation (checklist rapide)
- [ ] Je peux instancier `Filiere` et `Etudiant` sans erreur.
- [ ] Les setters refusent les valeurs invalides (id négatif, email invalide, champs vides).
- [ ] Je comprends le rôle d’une interface et de son implémentation.
- [ ] Je peux exécuter le scénario CLI et je vois les checkpoints attendus.
- [ ] La page `oop.php` fonctionne et affiche les checkpoints.
- [ ] Je sais expliquer l’encapsulation et pourquoi elle importe en modélisation métier.
