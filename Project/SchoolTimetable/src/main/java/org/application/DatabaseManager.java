package org.application;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.junit.jupiter.api.Assertions;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class DatabaseManager {
    private SessionFactory sessionFactory;

    public DatabaseManager() throws Exception {
        // A SessionFactory is set up once for an application!
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure() // configures settings from hibernate.cfg.xml
                .build();
        try {
            sessionFactory = new MetadataSources( registry )
                    .getMetadataBuilder()
                    .build()
                    .getSessionFactoryBuilder()
                    .build();
        }
        catch (Exception e) {
            // The registry would be destroyed by the SessionFactory, but we had trouble building the SessionFactory
            // so destroy it manually.
            StandardServiceRegistryBuilder.destroy( registry );
        }
    }

    public SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public boolean saveMessage(Message message)
    {
        try {
            Session session = sessionFactory.openSession();
            Transaction tx = session.beginTransaction();
            session.saveOrUpdate(message);
            tx.commit();
            session.close();
        }
        catch (Exception e) {
            // TODO: ?
            return false;
        }
        return true;
    }

    public List<Message> readMessages() {
        Session session = sessionFactory.openSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<Message> cq = cb.createQuery(Message.class);
        Root<Message> rootEntry = cq.from(Message.class);
        CriteriaQuery<Message> all = cq.select(rootEntry);

        TypedQuery<Message> allQuery = session.createQuery(all);
        List<Message> list = allQuery.getResultList();

        session.close();

        return list;
    }
}
