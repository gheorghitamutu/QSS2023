package org.dataaccess.database;

import jakarta.persistence.EntityManager;

/**
 * This is the interface for IHibernateProvider.
 */
public interface IHibernateProvider {

    /**
     * This is the method to get the entity manager.
     * @return The entity manager.
     */
    EntityManager getEntityManager();
}
