package org.application.dataaccess.repository;

import java.util.List;

public interface IRepository<T> {
    boolean save(T object);

    boolean delete(T object);

    boolean deleteMany(List<T> objects);

    List<T> readAll();
}

