package org.application.dataaccess;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public interface IHibernateProvider {

    public EntityManager getEntityManager();
}
