package org.application.dataaccess;

import com.google.inject.Inject;
import org.application.models.Student;


public class StudentsRepository extends BaseRepository<Student> implements IStudentsRepository {

    @Inject
    public StudentsRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);
    }

}
