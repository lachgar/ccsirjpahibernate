<?php
declare(strict_types=1);

require __DIR__ . '/bootstrap.php';

use App\Entity\Filiere;
use App\Entity\Etudiant;
use App\Repository\FakeRepository;
use App\Exception\ValidationException;

// Démo CLI pour LAB 2 — POO et Modélisation Métier

$repo = new FakeRepository();

// 1) Création d'une filière
$fInfo = new Filiere(1, 'Informatique');
$repo->create($fInfo);
checkpoint('Filiere créée', (string)$fInfo);

// 2) Création d'un étudiant
$eAlice = new Etudiant(1, 'Alice', 'alice@example.com', $fInfo->getId());
$repo->create($eAlice);
checkpoint('Etudiant créé', (string)$eAlice);

// 3) Lecture
$found = $repo->findById(Etudiant::class, 1);
checkpoint('Find Etudiant#1', $found ? (string)$found : 'null');

// 4) Mise à jour (setter avec validation)
$eAlice->setNom('Alice Dupont');
$repo->update($eAlice);
checkpoint('Etudiant mis à jour', (string)$repo->findById(Etudiant::class, 1));

// 5) Liste
$etudiants = $repo->findAll(Etudiant::class);
checkpoint('Liste des étudiants (count)', count($etudiants));

// 6) Validation: email invalide (déclenche une exception)
try {
    $eAlice->setEmail('invalide');
    checkpoint('Email modifié (devrait échouer)', 'OK');
} catch (ValidationException $ex) {
    checkpoint('Validation attendue (email)', $ex->getMessage());
}

// 7) Suppression
$repo->delete(Etudiant::class, 1);
checkpoint('Suppression Etudiant#1', $repo->findById(Etudiant::class, 1) === null);
