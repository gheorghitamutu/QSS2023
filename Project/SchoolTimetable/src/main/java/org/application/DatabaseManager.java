package org.application;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import java.util.List;
import java.util.Properties;
import java.util.Set;

public class DatabaseManager {
    //XML based configuration
    private static SessionFactory sessionFactory;

    //Annotation based configuration
    private static SessionFactory sessionAnnotationFactory;

    //Property based configuration
    private static SessionFactory sessionJavaConfigFactory;

    private static SessionFactory buildSessionFactory() {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            System.out.println("Hibernate Configuration loaded");

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            System.out.println("Hibernate serviceRegistry created");

            sessionFactory = configuration.buildSessionFactory(serviceRegistry);

            return sessionFactory;
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static SessionFactory buildSessionAnnotationFactory() {
        try {
            Configuration configuration = new Configuration();
            configuration.configure("hibernate.cfg.xml");
            System.out.println("Hibernate Annotation Configuration loaded");

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            System.out.println("Hibernate Annotation serviceRegistry created");

            sessionAnnotationFactory = configuration.buildSessionFactory(serviceRegistry);

            return sessionAnnotationFactory;
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    private static SessionFactory buildSessionJavaConfigFactory() {
        try {
            Configuration configuration = new Configuration();

            Properties props = new Properties();
            props.put("hibernate.dialect", "org.hibernate.dialect.DerbyDialect");
            props.put("hibernate.connection.driver_class", "org.apache.derby.jdbc.EmbeddedDriver");
            props.put("hibernate.connection.url", "jdbc:derby:testdb;create=true");
            props.put("hibernate.connection.username", "app");
            props.put("hibernate.connection.password", "app");
            props.put("hibernate.current_session_context_class", "thread");
            props.put("connection.pool_size", 5);
            props.put("hbm2ddl.auto", "create-drop");
            props.put("hibernate.show_sql", true);
            props.put("hibernate.format_sql", false);
            props.put("javax.persistence.schema-generation.database.action", "drop-and-create");

            configuration.setProperties(props);

            configuration.addAnnotatedClass(org.application.models.Room.class);
            configuration.addAnnotatedClass(org.application.models.Student.class);
            configuration.addAnnotatedClass(org.application.models.Session.class);
            configuration.addAnnotatedClass(org.application.models.Discipline.class);
            configuration.addAnnotatedClass(org.application.models.Timeslot.class);
            configuration.addAnnotatedClass(org.application.models.Teacher.class);
            configuration.addAnnotatedClass(org.application.models.Group.class);

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
            System.out.println("Hibernate Java Config serviceRegistry created");

            sessionJavaConfigFactory = configuration.buildSessionFactory(serviceRegistry);

            return sessionJavaConfigFactory;
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) sessionFactory = buildSessionFactory();
        return sessionFactory;
    }

    public static SessionFactory getSessionAnnotationFactory() {
        if (sessionAnnotationFactory == null) sessionAnnotationFactory = buildSessionAnnotationFactory();
        return sessionAnnotationFactory;
    }

    public static SessionFactory getSessionJavaConfigFactory() {
        if (sessionJavaConfigFactory == null) sessionJavaConfigFactory = buildSessionJavaConfigFactory();
        return sessionJavaConfigFactory;
    }

    public static <T> boolean save(T object) {
        ValidatorFactory validatorFactory = Validation.byDefaultProvider()
                .configure()
                .messageInterpolator(new ParameterMessageInterpolator())
                .buildValidatorFactory();
        Validator validator = validatorFactory.usingContext()
                .messageInterpolator(new ParameterMessageInterpolator())
                .getValidator();
        Set<ConstraintViolation<T>> constraintViolationsInvalidObject = validator.validate(object);
        for (ConstraintViolation<T> constraintViolation : constraintViolationsInvalidObject) {
            System.out.println(constraintViolation.getMessage());
        }
        if (constraintViolationsInvalidObject.size() > 0) {
            return false;
        }

        try {
            Session session = DatabaseManager.getSessionJavaConfigFactory().openSession();
            session.beginTransaction();
            session.save(object);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public static <T> boolean delete(T object) {
        try {
            Session session = DatabaseManager.getSessionJavaConfigFactory().openSession();
            session.beginTransaction();
            session.delete(object);
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public static <T> boolean deleteMany(List<T> objects) {
        try {
            Session session = DatabaseManager.getSessionJavaConfigFactory().openSession();
            session.beginTransaction();
            for (T object : objects) {
                session.delete(object);
            }
            session.getTransaction().commit();
            session.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public static <T> List<T> readAll(Class<T> tClass) {
        Session session = DatabaseManager.getSessionJavaConfigFactory().openSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(tClass);
        Root<T> rootEntry = cq.from(tClass);
        CriteriaQuery<T> all = cq.select(rootEntry);

        TypedQuery<T> allQuery = session.createQuery(all);
        List<T> list = allQuery.getResultList();

        session.close();

        return list;
    }
}
