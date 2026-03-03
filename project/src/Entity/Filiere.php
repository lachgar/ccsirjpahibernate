<?php
declare(strict_types=1);

namespace App\Entity;

use App\Exception\ValidationException;

/**
 * Représente une Filiere (ex: Informatique, Mathématiques)
 */
class Filiere
{
    /** @var int */
    private $id;
    /** @var string */
    private $libelle;

    public function __construct(int $id, string $libelle)
    {
        $this->setId($id);
        $this->setLibelle($libelle);
    }

    // Getters
    public function getId(): int
    {
        return $this->id;
    }

    public function getLibelle(): string
    {
        return $this->libelle;
    }

    // Setters (avec validation)
    public function setId(int $id): void
    {
        if ($id <= 0) {
            throw new ValidationException('L\'identifiant de la filière doit être strictement positif.');
        }
        $this->id = $id;
    }

    public function setLibelle(string $libelle): void
    {
        $libelle = trim($libelle);
        if ($libelle === '') {
            throw new ValidationException('Le libellé de la filière ne peut pas être vide.');
        }
        $this->libelle = $libelle;
    }

    public function __toString(): string
    {
        return sprintf('%d - %s', $this->id, $this->libelle);
    }
}
