package org.dataaccess.repository;

import org.domain.exceptions.RepositoryOperationException;

import java.util.List;


/**
 * An interface representing a generic repository for managing entity of type T.
 *
 * @param <T> The type of entity managed by the repository.
 */
public interface IRepository<T> {

    /**
     * Retrieves an entity by its ID.
     *
     * @param id The ID of the entity to retrieve.
     * @return The entity with the specified ID, or null if not found.
     */
    public T getById(int id);

    /**
     * Saves an entity in the repository.
     *
     * @param object The entity to save.
     * @throws RepositoryOperationException If an error occurs during the repository operation.
     */
    void save(T object) throws RepositoryOperationException;

    /**
     * Deletes an entity from the repository.
     *
     * @param object The entity to delete.
     * @throws RepositoryOperationException If an error occurs during the repository operation.
     */
    void delete(T object) throws RepositoryOperationException;


    /**
     * Deletes multiple entities from the repository.
     *
     * @param objects The entities list to delete.
     * @throws RepositoryOperationException If an error occurs during the repository operation.
     */
    void deleteMany(List<T> objects) throws RepositoryOperationException;

    /**
     * Retrieves all entity of type T from the repository.
     *
     * @return A list of all entity in the repository.
     */
    List<T> readAll();

    /**
     * Validates an entity.
     *
     * @param object The entity to validate.
     * @return True if the entity passes validation, false otherwise.
     */
    boolean validate(T object);
}

