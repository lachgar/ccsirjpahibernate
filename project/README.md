# LAB 1 — Fondations PHP 7 et Typage Strict (Projet)

Ce dossier contient le projet minimal utilisé dans le laboratoire. Il fonctionne avec PHP 7 strictement et ne dépend d’aucun framework.

## Structure
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

## Prérequis
- PHP 7.x (7.2+ recommandé)
- Extension mbstring activée

## Démarrage rapide

- Mode serveur Web interne (depuis la racine du dépôt):
  - Windows PowerShell:
    ```powershell
    php -S localhost:8000 -t project\public
    ```
  - Naviguez vers: http://localhost:8000/
  - Testez: http://localhost:8000/?name=ALICE

- Mode CLI (depuis la racine du dépôt):
  ```powershell
  php project\demo_cli.php "ALICE"
  ```

Vous devriez voir apparaître des lignes « CHECKPOINT » en CLI et, côté Web, des paragraphes « CHECKPOINT » ainsi qu’un message « Exception capturée » (démonstration d’exception volontaire).

## Environnements (dev vs prod)
Par défaut, l’environnement est « dev » avec affichage des erreurs.

- Forcer l’environnement « prod » sous PowerShell:
  ```powershell
  $env:APP_ENV = "prod"
  php project\demo_cli.php "ALICE"
  Remove-Item Env:APP_ENV
  ```

En « prod », `display_errors = 0` et `log_errors = 1` (voir `bootstrap.php` et `config/prod.php`).

## Points techniques couverts
- `declare(strict_types=1)`
- Types scalaires et types de retour
- `include` / `require` (via `bootstrap.php`)
- Autoload simple (PSR-4 minimal) pour l’espace de noms `App\\`
- Exceptions (ValidationException) vs warnings
- Configuration PHP selon l’environnement

## Référence du LAB
Le guide complet du laboratoire se trouve dans `project/LAB_1_Fondations_PHP7_Typage_Strict.md`.


## Références des LABs
- LAB 1: `project/LAB_1_Fondations_PHP7_Typage_Strict.md`
- LAB 2: `project/LAB_2_POO_et_Modelisation_Metier_PHP7.md`

## Démonstrations LAB 2
- Web: démarrer le serveur interne puis ouvrir http://localhost:8000/oop.php
- CLI: `php project\demo_oop.php`
