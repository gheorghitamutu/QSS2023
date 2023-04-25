package org.application.dataaccess;

import org.application.models.Student;

public class StudentsRepository extends BaseRepository<Student> implements IStudentsRepository {
    public StudentsRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);
    }

}
