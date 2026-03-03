<?php
declare(strict_types=1);

// Bootstrap global pour le LAB 1 — PHP 7 strict

// Détection de l'environnement (dev par défaut)
$env = getenv('APP_ENV') ?: 'dev';

// Configuration selon l'environnement
if ($env === 'prod') {
    error_reporting(E_ALL & ~E_NOTICE & ~E_STRICT & ~E_DEPRECATED);
    ini_set('display_errors', '0');
    ini_set('log_errors', '1');
} else {
    // dev
    error_reporting(E_ALL);
    ini_set('display_errors', '1');
    ini_set('log_errors', '0');
}

// Inclusion de la configuration spécifique (démontre require/include)
$cfgFile = __DIR__ . '/config/' . ($env === 'prod' ? 'prod.php' : 'dev.php');
// require provoque une erreur fatale si le fichier est manquant (ici attendu présent)
$config = require $cfgFile;
// Expose la config globalement pour les exemples
$GLOBALS['config'] = is_array($config) ? $config : [];

// Simple autoloader PSR-4 minimal pour l'espace de noms App\
spl_autoload_register(function (string $class): void {
    $prefix = 'App\\';
    $baseDir = __DIR__ . '/src/';
    $len = strlen($prefix);
    if (strncmp($class, $prefix, $len) !== 0) {
        return; // pas notre espace de noms
    }
    $relativeClass = substr($class, $len);
    $file = $baseDir . str_replace('\\', DIRECTORY_SEPARATOR, $relativeClass) . '.php';
    if (is_file($file)) {
        require_once $file;
    }
});

// Helpers fonctionnels (les fonctions ne sont pas autoloadées en natif)
require_once __DIR__ . '/src/helpers.php';

// Mise à disposition d'une fonction utilitaire de checkpoint
if (!function_exists('checkpoint')) {
    function checkpoint(string $label, $value): void {
        if (PHP_SAPI === 'cli') {
            echo "[CHECKPOINT] $label => ";
            if (is_bool($value)) {
                echo $value ? 'OK' : 'KO';
            } else {
                echo json_encode($value, JSON_UNESCAPED_UNICODE);
            }
            echo PHP_EOL;
        } else {
            echo '<p><strong>CHECKPOINT:</strong> ' . htmlspecialchars($label, ENT_QUOTES, 'UTF-8') . ' => ' . htmlspecialchars(is_scalar($value) ? (string)$value : json_encode($value, JSON_UNESCAPED_UNICODE), ENT_QUOTES, 'UTF-8') . '</p>';
        }
    }
}
