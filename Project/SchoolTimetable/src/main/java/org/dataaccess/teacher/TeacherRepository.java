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
 * A repository class for managing Teacher entities.
 * Extends the BaseRepository class with Teacher as the generic type
 * and implements the ITeacherRepository interface.
 * Contains methods for creating new Teacher entities or retrieving Teacher objects by their name.
 */
public class TeacherRepository extends BaseRepository<Teacher> implements ITeacherRepository {

    /**
     * Constructs a TeacherRepository object with the provided Hibernate provider.
     *
     * @param hibernateProvider The Hibernate provider used for data access.
     */
    @Inject
    public TeacherRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);
    }


    /**
     * Retrieves a Teacher entity by its name.
     *
     * @param name The name of the Teacher to retrieve.
     * @return The Teacher new entity with the specified name, or null if not found.
     * @throws RepositoryOperationException If an error occurs during the repository operation.
     * @throws ValidationException If the validation of the Teacher name fails.
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
     * Creates a new Teacher entity with the specified name and type.
     *
     * @param name The name of the new Teacher.
     * @param type The type of the new Teacher.
     * @return The newly created Teacher object.
     * @throws RepositoryOperationException If an error occurs during the repository operation.
     * @throws ValidationException If the validation of the Teacher attributes fails.
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
