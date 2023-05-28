package org.dataaccess.student;

import com.google.inject.Inject;
import org.application.helpers.ValidationHelpers;
import org.dataaccess.repository.BaseRepository;
import org.dataaccess.database.IHibernateProvider;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Student;
import org.domain.models.StudentGroup;

/**
 * A repository class for managing Student entities.
 * Extends the BaseRepository class with Student as the generic type and implements the IStudentRepository interface.
 * Contains methods for updating and deleting Student entities.
 */
public class StudentRepository extends BaseRepository<Student> implements IStudentRepository {

    /**
     * Constructs a new StudentRepository with the specified Hibernate provider.
     *
     * @param hibernateProvider The Hibernate provider for database access.
     */
    @Inject
    public StudentRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);
    }

    /**
     * Updates the information of a Student entity.
     *
     * @param student The Student entity to update.
     * @return The updated Student entity.
     * @throws RepositoryOperationException If an error occurs during the repository operation.
     * @throws ValidationException If the validation of the Student attributes fails.
     */
    @Override
    public Student updateStudent(Student student) throws RepositoryOperationException, ValidationException {

        ValidationHelpers.requireNotNull(student, IllegalArgumentException.class, "Student cannot be null.", null);
        ValidationHelpers.requirePositiveOrZero(student.getId(), IllegalArgumentException.class, "Student id cannot be negative.", null);
        ValidationHelpers.requireNotBlank(student.getName(), IllegalArgumentException.class, "Student first name cannot be blank.", null);
        ValidationHelpers.requireNotBlank(student.getRegistrationNumber(), IllegalArgumentException.class, "Student registration number cannot be blank.", null);

        var session = hibernateProvider.getEntityManager();

        if (!session.getTransaction().isActive()) {
            session.getTransaction().begin();
        }

        try {
            session.merge(student);
        } catch (Exception e) {
            e.printStackTrace();

            session.getTransaction().rollback();

            throw new RepositoryOperationException("[StudentRepository] Couldn't update student.", e);
        }

        session.getTransaction().commit();

        return student;
    }


    /**
     * Deletes a Student entity from the repository.
     *
     * @param student The Student entity to delete.
     * @return The deleted Student entity.
     * @throws RepositoryOperationException If an error occurs during the repository operation.
     * @throws ValidationException If the validation of the Student attributes fails.
     */
    @Override
    public Student deleteStudent(Student student) throws RepositoryOperationException, ValidationException {

        ValidationHelpers.requireNotNull(student, IllegalArgumentException.class, "Student cannot be null.", null);
        ValidationHelpers.requirePositiveOrZero(student.getId(), IllegalArgumentException.class, "Student id cannot be negative.", null);
        ValidationHelpers.requireNotBlank(student.getName(), IllegalArgumentException.class, "Student first name cannot be blank.", null);
        ValidationHelpers.requireNotBlank(student.getRegistrationNumber(), IllegalArgumentException.class, "Student registration number cannot be blank.", null);


        var session = hibernateProvider.getEntityManager();

        if (!session.getTransaction().isActive()) {
            session.getTransaction().begin();
        }

        try {
            session.remove(student);

        } catch (Exception e) {
            e.printStackTrace();

            session.getTransaction().rollback();

            throw new RepositoryOperationException("[StudentRepository] Couldn't delete student.", e);

        }

        session.getTransaction().commit();

        return student;
    }

    /**
     * Changes the StudentGroup of a Student entity.
     *
     * @param student   The Student entity for which to change the group.
     * @param newGroup  The new StudentGroup for the Student entity.
     * @return The updated Student entity with the new group.
     * @throws RepositoryOperationException If an error occurs during the repository operation.
     * @throws ValidationException If the validation of the Student attributes fails.
     */
    @Override
    public Student changeStudentGroup(Student student, StudentGroup newGroup) throws RepositoryOperationException, ValidationException {


        ValidationHelpers.requireNotNull(student, IllegalArgumentException.class, "Student cannot be null.", null);
        ValidationHelpers.requirePositiveOrZero(student.getId(), IllegalArgumentException.class, "Student id cannot be negative.", null);
        ValidationHelpers.requireNotBlank(student.getName(), IllegalArgumentException.class, "Student first name cannot be blank.", null);
        ValidationHelpers.requireNotBlank(student.getRegistrationNumber(), IllegalArgumentException.class, "Student registration number cannot be blank.", null);

        ValidationHelpers.requireNotNull(newGroup, IllegalArgumentException.class, "New group cannot be null.", null);
        ValidationHelpers.requirePositiveOrZero(newGroup.getId(), IllegalArgumentException.class, "New group id cannot be negative.", null);
        ValidationHelpers.requireNotBlank(newGroup.getName(), IllegalArgumentException.class, "New group name cannot be blank.", null);

        var session = hibernateProvider.getEntityManager();

        if (!session.getTransaction().isActive()) {
            session.getTransaction().begin();
        }

        try {
            student.setGroup(newGroup);

            session.persist(student);

            session.getTransaction().commit();

        } catch (Exception e) {
            e.printStackTrace();
            session.getTransaction().rollback();

            throw new RepositoryOperationException("[StudentRepository] Couldn't change student group.", e);
        }


        return student;
    }
}
