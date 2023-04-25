package org.application.dataaccess;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import jakarta.persistence.*;

import java.util.List;
import java.util.Set;

public class BaseRepository<T> implements IRepository<T> {


    private final IHibernateProvider hibernateProvider;

    protected BaseRepository(IHibernateProvider hibernateProvider)
    {

        this.hibernateProvider = hibernateProvider;
    }

    private boolean constraintValidation(T object) {
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

    public boolean save(T object) {
        if (!constraintValidation(object)) {
            return false;
        }

        try {
            var session = this.hibernateProvider.getEntityManager();

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

    public boolean delete(T object) {
        try {
            var session = this.hibernateProvider.getEntityManager();

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

    public boolean deleteMany(List<T> objects) {
        try {
            var session = this.hibernateProvider.getEntityManager();

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

    public List<T> readAll(Class<T> tClass) {
        var session = this.hibernateProvider.getEntityManager();

        CriteriaBuilder cb = session.getCriteriaBuilder();
        CriteriaQuery<T> cq = cb.createQuery(tClass);
        Root<T> rootEntry = cq.from(tClass);
        CriteriaQuery<T> all = cq.select(rootEntry);

        TypedQuery<T> allQuery = session.createQuery(all);
        List<T> list = allQuery.getResultList();

        return list;
    }
}
