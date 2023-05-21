package org.dataaccess.student;

import com.google.inject.Inject;
import jakarta.validation.Valid;
import org.dataaccess.repository.BaseRepository;
import org.dataaccess.database.IHibernateProvider;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.models.Student;
import org.domain.models.StudentGroup;

public class StudentRepository extends BaseRepository<Student> implements IStudentRepository {

    @Inject
    public StudentRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);
    }

    @Override
    public Student updateStudent(@Valid Student student) throws RepositoryOperationException {

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
    public Student deleteStudent(@Valid Student student) throws RepositoryOperationException {

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
    public Student changeStudentGroup(@Valid Student student, @Valid StudentGroup newGroup) throws RepositoryOperationException {

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
