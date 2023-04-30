package org.application.dataaccess.repository;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.validation.*;
import org.application.dataaccess.database.IHibernateProvider;
import org.application.domain.exceptions.RepositoryOperationException;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

public class BaseRepository<T> implements IRepository<T> {

    protected final IHibernateProvider hibernateProvider;
    protected Class<T> tClass;

    protected BaseRepository(IHibernateProvider hibernateProvider) {

        try {
            Type mySuperclass = getClass().getGenericSuperclass();
            Type tType = ((ParameterizedType) mySuperclass).getActualTypeArguments()[0];
            String className = tType.getTypeName();
            tClass = (Class<T>) Class.forName(className);
        } catch (Exception ex) {
            System.out.println("Class not found!");
        }


        this.hibernateProvider = hibernateProvider;
    }

    public T getById(int id) {
        var session = this.hibernateProvider.getEntityManager();
        return session.find(tClass, id);
    }

    public boolean validate(T object) {
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

    public void save(T object) throws RepositoryOperationException {
        if (!validate(object)) {
            throw new RepositoryOperationException("Validation fails for object");
        }

        try {
            var session = this.hibernateProvider.getEntityManager();

            if (!session.getTransaction().isActive()) {
                session.getTransaction().begin();
            }

            session.persist(object);

            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());

            throw new RepositoryOperationException("Save failed, check inner exception", e);
        }
    }

    public void delete(T object) throws RepositoryOperationException {
        try {
            var session = this.hibernateProvider.getEntityManager();

            if (!session.getTransaction().isActive()) {
                session.getTransaction().begin();
            }
            session.remove(object);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());

            throw new RepositoryOperationException("Delete failed, check inner exception", e);
        }
    }

    public void deleteMany(List<T> objects) throws RepositoryOperationException {
        try {
            var session = this.hibernateProvider.getEntityManager();

            if (!session.getTransaction().isActive()) {
                session.getTransaction().begin();
            }
            for (T object : objects) {
                session.remove(object);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());

            throw new RepositoryOperationException("Delete many failed, check inner exception", e);
        }
    }

    public List<T> readAll() {
        var session = this.hibernateProvider.getEntityManager();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(tClass);
        Root<T> rootEntry = cq.from(tClass);
        CriteriaQuery<T> all = cq.select(rootEntry);

        TypedQuery<T> allQuery = session.createQuery(all);

        return allQuery.getResultList();
    }
}
