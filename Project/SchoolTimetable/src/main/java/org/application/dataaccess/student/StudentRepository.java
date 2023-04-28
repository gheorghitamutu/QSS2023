package org.application.dataaccess.student;

import com.google.inject.Inject;
import org.application.dataaccess.repository.BaseRepository;
import org.application.dataaccess.hibernate.IHibernateProvider;
import org.application.models.Student;


public class StudentRepository extends BaseRepository<Student> implements IStudentRepository {

    @Inject
    public StudentRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);
    }

}
