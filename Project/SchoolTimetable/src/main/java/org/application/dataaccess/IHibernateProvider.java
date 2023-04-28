package org.application.dataaccess;

import jakarta.persistence.EntityManager;

public interface IHibernateProvider {

    EntityManager getEntityManager();
}
