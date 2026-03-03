<?php
declare(strict_types=1);

namespace App\Service;

final class GreetingService
{
    private string $appName;

    public function __construct(string $appName)
    {
        $this->appName = $appName;
    }

    public function greet(string $name): string
    {
        // Retourne un message typé string
        return sprintf('Bienvenue %s sur %s', $name, $this->appName);
    }
}
