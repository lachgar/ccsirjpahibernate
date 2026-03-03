<?php
declare(strict_types=1);

require __DIR__ . '/../bootstrap.php';

use App\Entity\Filiere;
use App\Entity\Etudiant;
use App\Repository\FakeRepository;
use App\Exception\ValidationException;

?><!DOCTYPE html>
<html lang="fr">
<head>
    <meta charset="UTF-8">
    <title>LAB 2 — Démo POO et Modélisation</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <style>
        body{font-family: Arial, sans-serif; margin: 2rem;}
        code{background:#f5f5f5; padding:2px 4px;}
        .section{margin-bottom:1.5rem;}
    </style>
</head>
<body>
<h1>LAB 2 — Démo POO et Modélisation</h1>
<p>Cette page exécute un scénario de test minimal des entités et du <code>FakeRepository</code>.</p>
<div class="section">
<?php
$repo = new FakeRepository();

// 1) Création Filiere
$fInfo = new Filiere(1, 'Informatique');
$repo->create($fInfo);
checkpoint('Filiere créée', (string)$fInfo);

// 2) Création Etudiant
$eBob = new Etudiant(1, 'Bob', 'bob@example.com', $fInfo->getId());
$repo->create($eBob);
checkpoint('Etudiant créé', (string)$eBob);

// 3) Lecture
$found = $repo->findById(Etudiant::class, 1);
checkpoint('Find Etudiant#1', $found ? (string)$found : 'null');

// 4) Mise à jour
$eBob->setNom('Bob Martin');
$repo->update($eBob);
checkpoint('Etudiant mis à jour', (string)$repo->findById(Etudiant::class, 1));

// 5) Validation (email invalide)
try {
    $eBob->setEmail('xxx');
    checkpoint('Email modifié (devrait échouer)', 'OK');
} catch (ValidationException $ex) {
    checkpoint('Validation attendue (email)', $ex->getMessage());
}

// 6) Suppression
$repo->delete(Etudiant::class, 1);
checkpoint('Suppression Etudiant#1', $repo->findById(Etudiant::class, 1) === null);
?>
</div>
<p>Retour à l'accueil: <a href="/">index.php</a></p>
</body>
</html>
