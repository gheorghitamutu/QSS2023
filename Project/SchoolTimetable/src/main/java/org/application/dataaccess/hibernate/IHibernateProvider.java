package org.application.dataaccess.hibernate;

import jakarta.persistence.EntityManager;

public interface IHibernateProvider {

    EntityManager getEntityManager();
}
