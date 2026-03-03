<?php
declare(strict_types=1);

// Point d'entrée Web du LAB 1
require_once __DIR__ . '/../bootstrap.php';

use App\Service\GreetingService;
use function App\addPositiveInts;
use function App\normalizeName;
use App\Exception\ValidationException;

$config = $GLOBALS['config'] ?? ['app_name' => 'LAB 1'];
$service = new GreetingService($config['app_name']);

$name = isset($_GET['name']) ? (string)$_GET['name'] : 'étudiant';

// Démonstration de types et fonctions
$normalized = null;
$sum = null;
$errorMessage = null;
try {
    $normalized = normalizeName($name);
    // Checkpoint attendu OK
    checkpoint('Nom normalisé', $normalized);

    $sum = addPositiveInts(3, 5);
    checkpoint('Somme 3 + 5', $sum);

    // Démonstration d'une exception volontaire
    addPositiveInts(-1, 2);
} catch (ValidationException $e) {
    $errorMessage = $e->getMessage();
    checkpoint('Exception capturée', $errorMessage);
}

$greeting = $service->greet($normalized ?? $name);

?><!doctype html>
<html lang="fr">
<head>
    <meta charset="utf-8">
    <title>LAB 1 — PHP 7 Strict</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style>
        body{font-family: system-ui, -apple-system, Segoe UI, Roboto, Helvetica, Arial, sans-serif; margin: 2rem;}
        code{background:#f5f5f5; padding:2px 4px; border-radius:3px}
        .ok{color: #0a7a2f}
        .warn{color: #7a4a0a}
    </style>
</head>
<body>
<h1>LAB 1 — Fondations PHP 7 et Typage Strict</h1>
<p><?= htmlspecialchars($greeting, ENT_QUOTES, 'UTF-8') ?></p>

<ul>
    <li>Environnement: <strong><?= htmlspecialchars((string)($config['env'] ?? 'dev'), ENT_QUOTES, 'UTF-8') ?></strong></li>
    <li>Affichage des erreurs: <strong><?= ini_get('display_errors') === '1' ? 'activé' : 'désactivé' ?></strong></li>
</ul>

<p>Essayez avec un paramètre de requête: <code>?name=ALICE</code></p>
<?php if ($errorMessage): ?>
    <p class="warn">Exception attendue capturée: <?= htmlspecialchars($errorMessage, ENT_QUOTES, 'UTF-8') ?></p>
<?php endif; ?>

<p class="ok">Tout est opérationnel si vous voyez les checkpoints ci-dessus.</p>
</body>
</html>
