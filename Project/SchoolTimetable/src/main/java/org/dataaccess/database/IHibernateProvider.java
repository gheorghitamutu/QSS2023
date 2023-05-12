package org.dataaccess.database;

import jakarta.persistence.EntityManager;

public interface IHibernateProvider {

    EntityManager getEntityManager();
}
