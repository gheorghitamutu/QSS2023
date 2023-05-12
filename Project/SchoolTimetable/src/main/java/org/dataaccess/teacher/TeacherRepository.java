package org.dataaccess.teacher;

import com.google.inject.Inject;
import org.dataaccess.database.IHibernateProvider;
import org.dataaccess.repository.BaseRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.models.Teacher;

import java.util.Date;

public class TeacherRepository extends BaseRepository<Teacher> implements ITeacherRepository {

    @Inject
    public TeacherRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);
    }

    public Teacher getByName(String name) throws RepositoryOperationException {
        var session = hibernateProvider.getEntityManager();
        var query = session.createNamedQuery("Teacher.getByName", Teacher.class);
        query.setParameter("name", name);

        var result = query.getResultList();
        if (result.size() == 0) {
            return null;
        }

        return result.get(0);
    }

    public Teacher createNewTeacher(String name, Teacher.Type type) throws RepositoryOperationException {
        var teacher = new Teacher();

        teacher.setName(name);
        teacher.setType(type);
        teacher.setInsertTime(new Date());

        try {
            save(teacher);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RepositoryOperationException("[TeacherRepository] Couldn't create new teacher.", e);
        }

        return teacher;
    }
}
