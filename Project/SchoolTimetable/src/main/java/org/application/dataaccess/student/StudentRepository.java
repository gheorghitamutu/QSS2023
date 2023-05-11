package org.application.dataaccess.student;

import com.google.inject.Inject;
import org.application.dataaccess.repository.BaseRepository;
import org.application.dataaccess.database.IHibernateProvider;
import org.application.domain.exceptions.RepositoryOperationException;
import org.application.domain.models.Student;
import org.application.domain.models.StudentGroup;

public class StudentRepository extends BaseRepository<Student> implements IStudentRepository {

    @Inject
    public StudentRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);
    }

    @Override
    public Student updateStudent(Student student) throws RepositoryOperationException {

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
    public Student deleteStudent(Student student) throws RepositoryOperationException {

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
    public Student changeStudentGroup(Student student, StudentGroup newGroup) throws RepositoryOperationException {

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
