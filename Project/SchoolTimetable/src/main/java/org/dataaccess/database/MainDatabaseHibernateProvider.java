package org.dataaccess.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class MainDatabaseHibernateProvider implements IHibernateProvider {

    public static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistence-unit");
    public EntityManager entityManager = entityManagerFactory.createEntityManager();

    @Override
    public EntityManager getEntityManager() {

        if (entityManager == null || !entityManager.isOpen()) {
            entityManager = entityManagerFactory.createEntityManager();
        }

        return entityManager;
    }
}
