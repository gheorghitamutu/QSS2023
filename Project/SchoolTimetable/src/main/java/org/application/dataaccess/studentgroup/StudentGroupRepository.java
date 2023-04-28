package org.application.dataaccess.studentgroup;

import com.google.inject.Inject;
import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.repository.BaseRepository;
import org.application.models.StudentGroup;

public class StudentGroupRepository extends BaseRepository<StudentGroup> implements IStudentGroupRepository {

    @Inject
    public StudentGroupRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);
    }

}
