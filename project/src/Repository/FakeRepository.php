<?php
declare(strict_types=1);

namespace App\Repository;

use App\Exception\ValidationException;

/**
 * FakeRepository — implémentation en mémoire d'un stockage de type repository.
 *
 * - Stocke des objets par classe dans des tableaux associatifs [id => objet].
 * - Suppose que les entités disposent d'un getter getId(): int.
 * - Ne gère aucune persistance disque; réinitialisée à chaque exécution.
 */
class FakeRepository implements RepositoryInterface
{
    /** @var array<string, array<int, object>> */
    private $store = [];

    public function create($entity)
    {
        $class = get_class($entity);
        $id = $this->extractId($entity);
        if ($id <= 0) {
            throw new ValidationException("L'identifiant doit être strictement positif pour la création.");
        }
        if (!isset($this->store[$class])) {
            $this->store[$class] = [];
        }
        if (isset($this->store[$class][$id])) {
            throw new ValidationException("Une entité de type $class avec l'id $id existe déjà.");
        }
        $this->store[$class][$id] = $entity;
        return $entity;
    }

    public function findById(string $entityClass, int $id)
    {
        if ($id <= 0) {
            throw new ValidationException("L'identifiant fourni doit être strictement positif.");
        }
        return $this->store[$entityClass][$id] ?? null;
    }

    public function findAll(string $entityClass): array
    {
        if (!isset($this->store[$entityClass])) {
            return [];
        }
        // Retourne les valeurs réindexées
        return array_values($this->store[$entityClass]);
    }

    public function update($entity): void
    {
        $class = get_class($entity);
        $id = $this->extractId($entity);
        if ($id <= 0) {
            throw new ValidationException("L'identifiant doit être strictement positif pour la mise à jour.");
        }
        if (!isset($this->store[$class][$id])) {
            throw new ValidationException("Impossible de mettre à jour: entité $class#$id introuvable.");
        }
        $this->store[$class][$id] = $entity;
    }

    public function delete(string $entityClass, int $id): void
    {
        if ($id <= 0) {
            throw new ValidationException("L'identifiant fourni doit être strictement positif.");
        }
        unset($this->store[$entityClass][$id]);
    }

    private function extractId($entity): int
    {
        if (!method_exists($entity, 'getId')) {
            throw new ValidationException('Entité invalide: méthode getId() requise.');
        }
        /** @var int $id */
        $id = $entity->getId();
        return (int)$id;
    }
}
