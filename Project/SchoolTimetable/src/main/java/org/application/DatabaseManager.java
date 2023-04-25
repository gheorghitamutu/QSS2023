package org.application;


//import jakarta.validation.ConstraintViolation;
//import jakarta.validation.Validation;
//import jakarta.validation.Validator;
//import jakarta.validation.ValidatorFactory;
//import org.application.models.StudentGroup;
//import org.hibernate.Session;
//import org.hibernate.SessionFactory;
//import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
//import org.hibernate.cfg.Configuration;
//import org.hibernate.service.ServiceRegistry;
//import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;
//
//import javax.persistence.TypedQuery;
//import javax.persistence.criteria.CriteriaBuilder;
//import javax.persistence.criteria.CriteriaQuery;
//import javax.persistence.criteria.Root;
//import java.util.List;
//import java.util.Properties;
//import java.util.Set;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import java.util.List;
import java.util.Set;

public class DatabaseManager {
    //XML based configuration
//    private static SessionFactory sessionFactory;
//
//    //Annotation based configuration
//    private static SessionFactory sessionAnnotationFactory;
//
//    //Property based configuration
//    private static SessionFactory sessionJavaConfigFactory;
//
//    private static SessionFactory buildSessionFactory() {
//        try {
//            // Create the SessionFactory from hibernate.cfg.xml
//            Configuration configuration = new Configuration();
//            configuration.configure("hibernate.cfg.xml");
//            System.out.println("Hibernate Configuration loaded");
//
//            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
//            System.out.println("Hibernate serviceRegistry created");
//
//            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
//
//            return sessionFactory;
//        } catch (Throwable ex) {
//            // Make sure you log the exception, as it might be swallowed
//            System.err.println("Initial SessionFactory creation failed." + ex);
//            throw new ExceptionInInitializerError(ex);
//        }
//    }
//
//    private static SessionFactory buildSessionAnnotationFactory() {
//        try {
//            Configuration configuration = new Configuration();
//            configuration.configure("hibernate.cfg.xml");
//            System.out.println("Hibernate Annotation Configuration loaded");
//
//            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
//            System.out.println("Hibernate Annotation serviceRegistry created");
//
//            sessionAnnotationFactory = configuration.buildSessionFactory(serviceRegistry);
//
//            return sessionAnnotationFactory;
//        } catch (Throwable ex) {
//            // Make sure you log the exception, as it might be swallowed
//            System.err.println("Initial SessionFactory creation failed." + ex);
//            throw new ExceptionInInitializerError(ex);
//        }
//    }
//
//    private static SessionFactory buildSessionJavaConfigFactory() {
//        try {
//            Configuration configuration = new Configuration();
//
//            Properties props = new Properties();
//            props.put("hibernate.dialect", "org.hibernate.dialect.DerbyDialect");
//            props.put("hibernate.connection.driver_class", "org.apache.derby.jdbc.EmbeddedDriver");
//            props.put("hibernate.connection.url", "jdbc:derby:testdb;create=true");
//            props.put("hibernate.connection.username", "app");
//            props.put("hibernate.connection.password", "app");
//            props.put("hibernate.current_session_context_class", "thread");
//            props.put("connection.pool_size", 5);
//            props.put("hbm2ddl.auto", "create-drop");
//            props.put("hibernate.show_sql", false);
//            props.put("hibernate.format_sql", false);
//            props.put("javax.persistence.schema-generation.database.action", "drop-and-create");
//
//            configuration.setProperties(props);
//
//            configuration.addAnnotatedClass(org.application.models.Room.class);
//            configuration.addAnnotatedClass(org.application.models.Student.class);
//            configuration.addAnnotatedClass(org.application.models.Session.class);
//            configuration.addAnnotatedClass(org.application.models.Discipline.class);
//            configuration.addAnnotatedClass(org.application.models.Timeslot.class);
//            configuration.addAnnotatedClass(org.application.models.Teacher.class);
//            configuration.addAnnotatedClass(StudentGroup.class);
//
//            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties()).build();
//            System.out.println("Hibernate Java Config serviceRegistry created");
//
//            sessionJavaConfigFactory = configuration.buildSessionFactory(serviceRegistry);
//
//            return sessionJavaConfigFactory;
//        } catch (Throwable ex) {
//            System.err.println("Initial SessionFactory creation failed." + ex);
//            throw new ExceptionInInitializerError(ex);
//        }
//    }
//
//    public static SessionFactory getSessionFactory() {
//        if (sessionFactory == null) sessionFactory = buildSessionFactory();
//        return sessionFactory;
//    }
//
//    public static SessionFactory getSessionAnnotationFactory() {
//        if (sessionAnnotationFactory == null) sessionAnnotationFactory = buildSessionAnnotationFactory();
//        return sessionAnnotationFactory;
//    }
//
//    public static SessionFactory getSessionJavaConfigFactory() {
//        if (sessionJavaConfigFactory == null) sessionJavaConfigFactory = buildSessionJavaConfigFactory();
//        return sessionJavaConfigFactory;
//    }
    public static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("persistence-unit-test");
    public static EntityManager entityManager = entityManagerFactory.createEntityManager();

    public static EntityManager getEntityManager() {

        if (entityManager == null || !entityManager.isOpen()) {
            entityManager = entityManagerFactory.createEntityManager();
        }

        return entityManager;
    }

    public static <T> boolean constraintValidation(T object) {
        Validator validator;
        try (ValidatorFactory validatorFactory = Validation.byDefaultProvider().configure().messageInterpolator(new ParameterMessageInterpolator()).buildValidatorFactory()) {
            validator = validatorFactory.usingContext().messageInterpolator(new ParameterMessageInterpolator()).getValidator();
        }
        if (validator == null) {
            return false;
        }

        Set<ConstraintViolation<T>> constraintViolationsInvalidObject = validator.validate(object);
        for (ConstraintViolation<T> constraintViolation : constraintViolationsInvalidObject) {
            System.out.println(constraintViolation.getMessage());
        }

        return constraintViolationsInvalidObject.size() == 0;
    }

    public static <T> boolean save(T object) {
        if (!constraintValidation(object)) {
            return false;
        }

        try {
            var session = getEntityManager();

            if (!session.getTransaction().isActive())
            {
                session.getTransaction().begin();
            }

            session.persist(object);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public static <T> boolean delete(T object) {
        try {
            var session = getEntityManager();

            if (!session.getTransaction().isActive())
            {
                session.getTransaction().begin();
            }
            session.remove(object);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public static <T> boolean deleteMany(List<T> objects) {
        try {
            var session = getEntityManager();

            if (!session.getTransaction().isActive())
            {
                session.getTransaction().begin();
            }
            for (T object : objects) {
                session.remove(object);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    public static <T> List<T> readAll(Class<T> tClass) {
        var session = getEntityManager();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(tClass);
        Root<T> rootEntry = cq.from(tClass);
        CriteriaQuery<T> all = cq.select(rootEntry);

        TypedQuery<T> allQuery = session.createQuery(all);
        List<T> list = allQuery.getResultList();

        return list;
    }
}
