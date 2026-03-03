# LAB 1 — Fondations PHP 7 et Typage Strict

Ce laboratoire suit la structure et l’esprit d’un Google Cloud Skills Boost Lab. Il est prêt à l’emploi pour une séance de TP, sans framework, avec PHP 7 strictement.

## Objectifs pédagogiques
- Comprendre et activer `declare(strict_types=1)`.
- Utiliser les types scalaires et les types de retour (PHP 7).
- Différencier `include` et `require` et savoir les utiliser à bon escient.
- Mettre en place un autoload simple (style PSR‑4 minimal).
- Distinguer exceptions vs warnings et gérer les exceptions.
- Paramétrer PHP pour les modes Développement vs Production.
- Construire et tester un mini‑projet exécutable via navigateur et CLI.

## Prérequis techniques
- PHP 7.x installé (7.2+ recommandé) avec ext/mbstring.
- Accès à une ligne de commande (PowerShell, Terminal) et un navigateur.
- Serveur PHP interne (php -S) ou équivalent.

## Durée estimée
- 45 à 60 minutes.

## Environnement requis
- OS au choix (Windows, macOS, Linux).
- Éditeur/IDE (JetBrains PhpStorm recommandé) avec accès au projet.

---

## Structure du projet
Le projet est déjà initialisé pour vous.

```
project/
  bootstrap.php            # strict_types, config env, autoload, helpers, checkpoints
  demo_cli.php             # démonstration CLI
  public/
    index.php              # point d’entrée Web
  config/
    dev.php                # configuration dev
    prod.php               # configuration prod
  src/
    helpers.php            # fonctions typées + validation
    Exception/
      ValidationException.php
    Service/
      GreetingService.php
```

Remarque pédagogique: Les fonctions (helpers) ne sont pas autoloadées nativement par PHP; on les charge via `require` dans `bootstrap.php`. Les classes sont chargées via un autoload PSR‑4 minimal.

---

## Étape 1 — Activer le typage strict

Explication: Le mode strict force PHP à respecter les types scalaires passés à vos fonctions/méthodes.

Instructions:
1. Ouvrez `project/src/helpers.php`. Notez la première ligne après `<?php`:
   ```php
   declare(strict_types=1);
   ```
2. Repérez les signatures typées, par exemple:
   ```php
   function addPositiveInts(int $a, int $b): int { ... }
   ```

Checkpoint (attendu): Vous constatez `declare(strict_types=1)` et des types pour paramètres et retours.

Erreur fréquente: Oublier `declare(strict_types=1)` en tête de fichier. Solution: Ajoutez la ligne tout en haut, avant tout autre code.

---

## Étape 2 — Configurer les environnements (dev vs prod)

Explication: En dev, on affiche les erreurs; en prod, on les log et on masque l’affichage.

Instructions:
1. Ouvrez `project/bootstrap.php` et observez la détection d’environnement via `APP_ENV`.
2. Ouvrez `project/config/dev.php` et `project/config/prod.php` pour comparer.
3. En ligne de commande, exécutez:
   - Dev (par défaut):
     ```bash
     php project\demo_cli.php "ALICE"
     ```
   - Prod:
     ```bash
     $env:APP_ENV="prod"; php project\demo_cli.php "ALICE"; Remove-Item Env:APP_ENV
     ```

Checkpoint: En dev, `display_errors=1`. En prod, `display_errors=0` et `log_errors=1` (visible via `phpinfo()` ou `ini_get`).

Erreur fréquente: L’ENV n’est pas pris en compte dans PowerShell. Solution: Utilisez `Env:` comme montré ci‑dessus.

---

## Étape 3 — include/require et autoload simple

Explication: `require` déclenche une erreur fatale si le fichier manque (utile pour des dépendances critiques). L’autoload charge les classes à la volée.

Instructions:
1. Dans `bootstrap.php`, identifiez:
   ```php
   $cfgFile = __DIR__ . '/config/' . ($env === 'prod' ? 'prod.php' : 'dev.php');
   $config = require $cfgFile; // require: fatal si absent
   ```
2. Observez l’autoload style PSR‑4 minimal:
   ```php
   spl_autoload_register(function (string $class): void {
       $prefix = 'App\\';
       $baseDir = __DIR__ . '/src/';
       ...
   });
   ```
3. Notez que les fonctions helpers sont chargées par:
   ```php
   require_once __DIR__ . '/src/helpers.php';
   ```

Checkpoint: Charger la page `public/index.php` dans le navigateur affiche les checkpoints sans erreur d’autoload.

Erreur fréquente: Arborescence/namespaces non cohérents (`App\Service` → `src/Service`). Solution: Respecter la casse et le chemin.

---

## Étape 4 — Fonctions typées et validation (avec exception)

Explication: On écrit des fonctions avec paramètres et retours typés, et on gère les erreurs métier via une exception dédiée.

Instructions:
1. Ouvrez `project/src/helpers.php` et repérez:
   ```php
   use App\Exception\ValidationException;
   function addPositiveInts(int $a, int $b): int {
       if ($a <= 0 || $b <= 0) {
           throw new ValidationException('Les deux entiers doivent être strictement positifs.');
       }
       return $a + $b;
   }
   ```
2. Dans `project/public/index.php`, observez l’appel volontaire fautif:
   ```php
   addPositiveInts(-1, 2); // doit déclencher une ValidationException
   ```
3. Rechargez la page ou exécutez en CLI:
   ```bash
   php project\demo_cli.php "bob"
   ```

Checkpoint: Un message « Exception capturée » s’affiche, prouvant la gestion d’exception.

Erreur fréquente: Attraper `\Exception` trop large. Solution: Attraper la classe dédiée `ValidationException`.

---

## Étape 5 — Vérification via navigateur et CLI

Explication: On valide le bon fonctionnement sur les deux canaux d’exécution courants.

Instructions:
1. Serveur web interne PHP (depuis la racine du dépôt):
   - Windows PowerShell:
     ```powershell
     php -S localhost:8000 -t project\public
     ```
   - Naviguez vers http://localhost:8000/ et testez `?name=ALICE`.
2. Mode CLI:
   ```powershell
   php project\demo_cli.php "ALICE"
   ```

Checkpoint: Vous voyez des lignes [CHECKPOINT] en CLI et, côté web, des paragraphes « CHECKPOINT » et un message d’exception attendue.

Erreur fréquente: `mbstring` manquante pour la normalisation du nom. Solution: Activer/ext installer `mbstring` ou remplacer par fonctions non‑multioctets (moins correct pour l’UTF‑8).

---

## Étape 6 — Exceptions vs warnings

Explication: Une exception interrompt le flot normal et doit être capturée; un warning est une alerte d’exécution mais le script continue.

Instructions (observation):
1. Forcez une exception (déjà fait via `addPositiveInts(0,1)`).
2. Pour observer un warning (optionnel), appelez une fonction avec mauvais nombre d’arguments dans un script temporaire et comparez le comportement.

Checkpoint: Différencier visuellement l’arrêt contrôlé par `try/catch` vs la continuité malgré un warning.

Remarque pédagogique: En mode prod, `display_errors` est désactivé; ne comptez pas sur l’affichage des warnings à l’écran.

---

## Résumé final
Vous avez:
- Activé et utilisé `strict_types`.
- Manipulé des fonctions et méthodes typées.
- Mis en place un autoload simple.
- Pratiqué `require`/`include` via le bootstrap et les helpers.
- Géré une exception applicative et constaté la différence avec un warning.
- Validé dev vs prod et exécuté le projet via navigateur et CLI.

## Livrables attendus
- Projet fonctionnel (dossier `project/` prêt à exécuter).
- Un fichier README décrivant la structure et les commandes (fourni: `project/README.md`).

## Auto‑évaluation (checklist)
- [ ] Tous les fichiers critiques contiennent `declare(strict_types=1)`.
- [ ] Je peux lancer `php -S localhost:8000 -t project/public` et voir les checkpoints.
- [ ] `php project/demo_cli.php "ALICE"` affiche des checkpoints et capture l’exception.
- [ ] Le changement d’environnement via `APP_ENV` modifie l’affichage des erreurs.
- [ ] L’autoload charge automatiquement `App\Service\GreetingService`.
- [ ] Je peux expliquer la différence entre exception et warning en PHP 7.
