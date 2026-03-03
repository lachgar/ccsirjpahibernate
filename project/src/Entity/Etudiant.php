<?php
declare(strict_types=1);

namespace App\Entity;

use App\Exception\ValidationException;

class Etudiant
{
    /** @var int */
    private $id;
    /** @var string */
    private $nom;
    /** @var string */
    private $email;
    /** @var int */
    private $filiereId;

    public function __construct(int $id, string $nom, string $email, int $filiereId)
    {
        $this->setId($id);
        $this->setNom($nom);
        $this->setEmail($email);
        $this->setFiliereId($filiereId);
    }

    // Getters
    public function getId(): int
    {
        return $this->id;
    }

    public function getNom(): string
    {
        return $this->nom;
    }

    public function getEmail(): string
    {
        return $this->email;
    }

    public function getFiliereId(): int
    {
        return $this->filiereId;
    }

    // Setters avec validation
    public function setId(int $id): void
    {
        if ($id <= 0) {
            throw new ValidationException("L'identifiant étudiant doit être strictement positif.");
        }
        $this->id = $id;
    }

    public function setNom(string $nom): void
    {
        $nom = trim($nom);
        if ($nom === '') {
            throw new ValidationException('Le nom ne peut pas être vide.');
        }
        $this->nom = $nom;
    }

    public function setEmail(string $email): void
    {
        $email = trim($email);
        if ($email === '') {
            throw new ValidationException("L'email ne peut pas être vide.");
        }
        if (filter_var($email, FILTER_VALIDATE_EMAIL) === false) {
            throw new ValidationException("Le format de l'email est invalide.");
        }
        $this->email = $email;
    }

    public function setFiliereId(int $filiereId): void
    {
        if ($filiereId <= 0) {
            throw new ValidationException("L'identifiant de la filière doit être strictement positif.");
        }
        $this->filiereId = $filiereId;
    }

    public function __toString(): string
    {
        return sprintf('%d - %s <%s> (filiere: %d)', $this->id, $this->nom, $this->email, $this->filiereId);
    }
}
