package org.application.dataaccess.teacher;

import com.google.inject.Inject;
import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.repository.BaseRepository;
import org.application.domain.models.Teacher;

public class TeacherRepository extends BaseRepository<Teacher> implements ITeacherRepository {

    @Inject
    public TeacherRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);
    }

}
