package org.application.dataaccess.repository;

import org.application.domain.exceptions.RepositoryOperationException;

import java.util.List;

public interface IRepository<T> {

    public T getById(int id);

    void save(T object) throws RepositoryOperationException;

    void delete(T object) throws RepositoryOperationException;

    void deleteMany(List<T> objects) throws RepositoryOperationException;

    List<T> readAll();

    boolean validate(T object);
}

