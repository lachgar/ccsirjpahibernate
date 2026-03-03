<?php
declare(strict_types=1);

namespace App; 

use App\Exception\ValidationException;

/**
 * Additionne deux entiers strictement positifs.
 *
 * - Démonstration des types scalaires et du type de retour (int)
 * - Validation applicative avec exception typée
 */
function addPositiveInts(int $a, int $b): int
{
    if ($a <= 0 || $b <= 0) {
        throw new ValidationException('Les deux entiers doivent être strictement positifs.');
    }
    return $a + $b;
}

/**
 * Normalise un nom proprement (exemple de type string de retour)
 */
function normalizeName(string $name): string
{
    $trimmed = trim($name);
    if ($trimmed === '') {
        throw new ValidationException('Le nom ne peut pas être vide.');
    }
    // Première lettre en majuscule, reste en minuscules (UTF-8 simplifié)
    $lower = mb_strtolower($trimmed, 'UTF-8');
    $first = mb_strtoupper(mb_substr($lower, 0, 1, 'UTF-8'), 'UTF-8');
    return $first . mb_substr($lower, 1, null, 'UTF-8');
}
