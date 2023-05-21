package org.dataaccess.student;

import com.google.inject.Inject;
import jakarta.validation.Valid;
import org.application.helpers.ValidationHelpers;
import org.dataaccess.repository.BaseRepository;
import org.dataaccess.database.IHibernateProvider;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Student;
import org.domain.models.StudentGroup;

public class StudentRepository extends BaseRepository<Student> implements IStudentRepository {

    @Inject
    public StudentRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);
    }

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
