<?php
declare(strict_types=1);

require __DIR__ . '/bootstrap.php';

use App\Service\GreetingService;
use App\Exception\ValidationException;
use function App\addPositiveInts;
use function App\normalizeName;

$config = $GLOBALS['config'] ?? ['app_name' => 'LAB 1'];
$service = new GreetingService($config['app_name']);

$name = $argv[1] ?? 'etudiant CLI';

try {
    $normalized = normalizeName($name);
    checkpoint('CLI Nom normalisé', $normalized);

    $sum = addPositiveInts(10, 20);
    checkpoint('CLI Somme 10 + 20', $sum);

    // Provoquer une exception
    addPositiveInts(0, 1);
} catch (ValidationException $e) {
    checkpoint('CLI Exception capturée', $e->getMessage());
}

$greeting = $service->greet($normalized ?? $name);
checkpoint('CLI Greeting', $greeting);
