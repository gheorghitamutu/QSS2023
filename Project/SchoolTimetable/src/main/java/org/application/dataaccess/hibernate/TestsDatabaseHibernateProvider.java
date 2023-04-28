package org.application.dataaccess.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class TestsDatabaseHibernateProvider implements IHibernateProvider {

    public static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistence-unit-test");
    public EntityManager entityManager = entityManagerFactory.createEntityManager();

    @Override
    public EntityManager getEntityManager() {

        if (entityManager == null || !entityManager.isOpen()) {
            entityManager = entityManagerFactory.createEntityManager();
        }

        return entityManager;
    }
}
