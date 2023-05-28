package org.dataaccess.teacher;

import org.dataaccess.repository.IRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Teacher;

/**
 * This is the interface for Teacher.
 */
public interface ITeacherRepository extends IRepository<Teacher> {

    /**
     * This is the method to get a teacher by name.
     * @param name The name.
     * @return The teacher.
     * @throws RepositoryOperationException The repository operation exception.
     * @throws ValidationException The validation exception.
     */
    public Teacher getByName(String name) throws RepositoryOperationException, ValidationException;

    /**
     * This is the method to create a new teacher.
     * @param name The name.
     * @param type The type.
     * @return The teacher.
     * @throws RepositoryOperationException The repository operation exception.
     * @throws ValidationException The validation exception.
     */
    public Teacher createNewTeacher(String name, Teacher.Type type) throws RepositoryOperationException, ValidationException;
}
