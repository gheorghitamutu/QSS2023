package org.dataaccess.teacher;

import com.google.inject.Inject;
import org.application.helpers.ValidationHelpers;
import org.dataaccess.database.IHibernateProvider;
import org.dataaccess.repository.BaseRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Teacher;

import java.util.Date;

/**
 * This is the class for TeacherRepository.
 */
public class TeacherRepository extends BaseRepository<Teacher> implements ITeacherRepository {

    /**
     * This is the constructor of TeacherRepository.
     * @param hibernateProvider The hibernate provider.
     */
    @Inject
    public TeacherRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);
    }

    /**
     * This is the method to get a teacher by name.
     * @param name The name.
     * @return The teacher.
     * @throws RepositoryOperationException The repository operation exception.
     * @throws ValidationException The validation exception.
     */
    public Teacher getByName(String name) throws RepositoryOperationException, ValidationException {

        ValidationHelpers.requireNotBlank(name, IllegalArgumentException.class, "Teacher name cannot be blank.", null);

        var session = hibernateProvider.getEntityManager();
        var query = session.createNamedQuery("Teacher.getByName", Teacher.class);
        query.setParameter("name", name);

        var result = query.getResultList();
        if (result.size() == 0) {
            return null;
        }

        return result.get(0);
    }

    /**
     * This is the method to create a new teacher.
     * @param name The name.
     * @param type The type.
     * @return The teacher.
     * @throws RepositoryOperationException The repository operation exception.
     * @throws ValidationException The validation exception.
     */
    public Teacher createNewTeacher(String name, Teacher.Type type) throws RepositoryOperationException, ValidationException {
        ValidationHelpers.requireNotBlank(name, IllegalArgumentException.class, "Teacher name cannot be blank.", null);
        ValidationHelpers.requireNotNull(type, IllegalArgumentException.class, "Teacher type cannot be null.", null);

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
