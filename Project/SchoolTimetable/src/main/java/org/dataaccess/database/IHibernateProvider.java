package org.dataaccess.database;

import jakarta.persistence.EntityManager;


/**
 * The IHibernateProvider interface provides a contract for classes that serve as providers of the Hibernate EntityManager.
 * It declares a single method getEntityManager() for obtaining the EntityManager instance that is used for database operations
 * on a specific database.
 */
public interface IHibernateProvider {

    /**
     * Returns the EntityManager instance that is used for database operations on a specific database.
     * @return The EntityManager instance that is used for database operations on a specific database.
     */
    EntityManager getEntityManager();
}
