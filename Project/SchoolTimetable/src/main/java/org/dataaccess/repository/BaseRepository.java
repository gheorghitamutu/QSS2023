package org.dataaccess.repository;

import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import jakarta.validation.*;
import org.dataaccess.database.IHibernateProvider;
import org.domain.exceptions.RepositoryOperationException;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

/**
 * This is the class for BaseRepository.
 * @param <T> The type.
 */
public class BaseRepository<T> implements IRepository<T> {

    /**
     * This is the hibernate provider.
     */
    protected final IHibernateProvider hibernateProvider;

    /**
     * This is the class.
     */
    protected Class<T> tClass;

    /**
     * This is the constructor of BaseRepository.
     * @param hibernateProvider The hibernate provider.
     */
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

    /**
     * This is the method to get an object by id.
     * @param id The id.
     * @return The object.
     */
    public T getById(int id) {
        var session = this.hibernateProvider.getEntityManager();
        return session.find(tClass, id);
    }

    /**
     * This method validates the object passed as input.
     * @param object The object.
     * @return True if the object is valid, false otherwise.
     */
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

    /**
     * This method saves the object passed as input into the database.
     * @param object The object.
     * @throws RepositoryOperationException The repository operation exception.
     */
    public void save(T object) throws RepositoryOperationException {

        // preconditions

        if (!validate(object)) {
            throw new RepositoryOperationException("Validation fails for object");
        }

        var session = this.hibernateProvider.getEntityManager();

        try {

            if (!session.getTransaction().isActive()) {
                session.getTransaction().begin();
            }

            session.persist(object);

            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());

            session.getTransaction().rollback();

            throw new RepositoryOperationException("Save failed, check inner exception", e);
        }

        // postconditions
        if (!validate(object)) {
            throw new RepositoryOperationException("DB left in inconsistent state after save");
        }
    }

    /**
     * This method deletes the object passed as input from the database.
     * @param object The object.
     * @throws RepositoryOperationException The repository operation exception.
     */
    public void delete(T object) throws RepositoryOperationException {

        var session = this.hibernateProvider.getEntityManager();

        try {

            if (!session.getTransaction().isActive()) {
                session.getTransaction().begin();
            }
            session.remove(object);
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());

            session.getTransaction().rollback();
            throw new RepositoryOperationException("Delete failed, check inner exception", e);
        }
    }

    /**
     * This method deletes the list of objects passed as input from the database.
     * @param objects The objects.
     * @throws RepositoryOperationException The repository operation exception.
     */
    public void deleteMany(List<T> objects) throws RepositoryOperationException {
        var session = this.hibernateProvider.getEntityManager();

        try {
            if (!session.getTransaction().isActive()) {
                session.getTransaction().begin();
            }
            for (T object : objects) {
                session.remove(object);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());

            session.getTransaction().rollback();

            throw new RepositoryOperationException("Delete many failed, check inner exception", e);
        }
    }

    /**
     * This method gets all the objects from the database.
     * @return The list of objects.
     */
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
