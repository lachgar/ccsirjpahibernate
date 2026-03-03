<?php
declare(strict_types=1);

namespace App\Repository;

/**
 * Interface de base pour un repository générique (stockage abstrait).
 * Les implémentations peuvent cibler un type spécifique ou plusieurs types d'entités.
 */
interface RepositoryInterface
{
    /**
     * Persiste une entité en stockage et retourne éventuellement l'entité (éventuellement enrichie).
     * @param object $entity
     * @return object
     */
    public function create($entity);

    /**
     * Récupère une entité par son identifiant ou null si inexistante.
     * @param string $entityClass Nom de classe pleinement qualifié (FQCN)
     * @param int $id Identifiant positif
     * @return object|null
     */
    public function findById(string $entityClass, int $id);

    /**
     * Liste toutes les entités d'un type.
     * @param string $entityClass
     * @return array Liste d'objets du type demandé
     */
    public function findAll(string $entityClass): array;

    /**
     * Met à jour une entité existante en stockage.
     * @param object $entity
     */
    public function update($entity): void;

    /**
     * Supprime une entité par identifiant.
     * @param string $entityClass
     * @param int $id
     */
    public function delete(string $entityClass, int $id): void;
}
