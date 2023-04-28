package org.application.dataaccess;

import com.google.inject.Inject;
import org.application.models.Student;


public class StudentRepository extends BaseRepository<Student> implements IStudentRepository {

    @Inject
    public StudentRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);
    }

}
