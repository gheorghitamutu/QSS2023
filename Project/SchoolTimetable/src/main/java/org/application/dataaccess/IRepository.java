package org.application.dataaccess;

import java.util.List;

public interface IRepository<T> {
    public boolean save(T object);

    public boolean delete(T object);

    public boolean deleteMany(List<T> objects);

    public List<T> readAll(Class<T> tClass);
}

