package org.dataaccess.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * Provides implementation of the IHibernateProvider interface.
 * It is used for obtaining the EntityManager instance that is used for database operations on the main database.
 * Points to the main database (production).
 */
public class MainDatabaseHibernateProvider implements IHibernateProvider {

    /**
     * This is the entity manager factory.
     */
    public static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistence-unit");

    /**
     * This is the entity manager.
     */
    public EntityManager entityManager = entityManagerFactory.createEntityManager();

    /**
     * Retrieves the EntityManager instance. Creates before returning if it is not created yet.
     *
     * @return The current EntityManager instance.
     */
    @Override
    public EntityManager getEntityManager() {

        if (entityManager == null || !entityManager.isOpen()) {
            entityManager = entityManagerFactory.createEntityManager();
        }

        return entityManager;
    }
}
