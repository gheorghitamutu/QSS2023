package org.dataaccess.repository;

import org.domain.exceptions.RepositoryOperationException;

import java.util.List;

/**
 * This is the interface for Repository.
 * @param <T> The type.
 */
public interface IRepository<T> {

    /**
     * This is the method to get an object by id.
     * @param id The id.
     * @return The object.
     */
    public T getById(int id);

    /**
     * This is the method to save an object.
     * @param object The object.
     * @throws RepositoryOperationException The repository operation exception.
     */
    void save(T object) throws RepositoryOperationException;

    /**
     * This is the method to update an object.
     * @param object The object.
     * @throws RepositoryOperationException The repository operation exception.
     */
    void delete(T object) throws RepositoryOperationException;

    /**
     * This is the method to delete many objects.
     * @param objects The objects.
     * @throws RepositoryOperationException The repository operation exception.
     */
    void deleteMany(List<T> objects) throws RepositoryOperationException;

    /**
     * This is the method to read all objects.
     * @return The objects.
     */
    List<T> readAll();

    /**
     * This is the method to validate an object.
     * @param object The object.
     * @return The boolean.
     */
    boolean validate(T object);
}

