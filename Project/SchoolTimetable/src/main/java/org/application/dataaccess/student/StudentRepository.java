package org.application.dataaccess.student;

import com.google.inject.Inject;
import org.application.dataaccess.repository.BaseRepository;
import org.application.dataaccess.database.IHibernateProvider;
import org.application.domain.models.Student;
import org.application.domain.models.StudentGroup;

public class StudentRepository extends BaseRepository<Student> implements IStudentRepository {

    @Inject
    public StudentRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);
    }

    @Override
    public Student updateStudent(Student student) {

        var session = hibernateProvider.getEntityManager();

        if (!session.getTransaction().isActive()) {
            session.getTransaction().begin();
        }

        session.merge(student);

        session.getTransaction().commit();

        return student;
    }

    @Override
    public Student deleteStudent(Student student) {

        var session = hibernateProvider.getEntityManager();

        if (!session.getTransaction().isActive()) {
            session.getTransaction().begin();
        }

        session.remove(student);

        session.getTransaction().commit();

        return student;
    }

    @Override
    public Student changeStudentGroup(Student student, StudentGroup newGroup) {

        var session = hibernateProvider.getEntityManager();

        if (!session.getTransaction().isActive()) {
            session.getTransaction().begin();
        }

        student.setGroup(newGroup);

        session.merge(student);

        session.getTransaction().commit();

        return student;
    }
}
