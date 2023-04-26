package org.application.dataaccess;

import jakarta.persistence.EntityManager;

public interface IHibernateProvider {

    public EntityManager getEntityManager();
}
