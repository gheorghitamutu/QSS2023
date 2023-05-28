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
 * A generic base repository class that provides common functionality for managing entitiies of type T.
 *
 * @param <T> The type of entitiy managed by the repository.
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
     * Creates a new instance of BaseRepository with the specified Hibernate provider.
     *
     * @param hibernateProvider The Hibernate provider to use for data access.
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
     * Retrieves an entity by its ID.
     *
     * @param id The ID of the entity to retrieve.
     * @return The entity with the specified ID, or null if not found.
     */
    public T getById(int id) {
        var session = this.hibernateProvider.getEntityManager();
        return session.find(tClass, id);
    }

    /**
     * Validates an entity using the configured validator.
     *
     * @param object The entity to validate.
     * @return True if the entity passes validation, false otherwise.
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
     * Saves an entity in the repository.
     *
     * @param object The entity to save.
     * @throws RepositoryOperationException If an error occurs during the repository operation.
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
     * Deletes an entity from the repository.
     *
     * @param object The entity to delete.
     * @throws RepositoryOperationException If an error occurs during the repository operation.
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
     * Deletes multiple entities from the repository.
     *
     * @param objects The entities to delete.
     * @throws RepositoryOperationException If an error occurs during the repository operation.
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
     * Retrieves all entities of type T from the repository.
     *
     * @return A list of all entities in the repository.
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
