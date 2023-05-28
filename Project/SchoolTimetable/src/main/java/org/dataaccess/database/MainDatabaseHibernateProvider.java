package org.dataaccess.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 * This is the class for MainDatabaseHibernateProvider.
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
     * This is the method to get the entity manager.
     * @return The entity manager.
     */
    @Override
    public EntityManager getEntityManager() {

        if (entityManager == null || !entityManager.isOpen()) {
            entityManager = entityManagerFactory.createEntityManager();
        }

        return entityManager;
    }
}
