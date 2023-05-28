package org.dataaccess.teacher;

import org.dataaccess.repository.IRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Teacher;

/**
 * A repository interface for managing Teacher entities.
 * Extends the IRepository interface with Teacher as the generic type.
 * This interface defines the repository operations that can be performed, specific to teacher repository.
 */
public interface ITeacherRepository extends IRepository<Teacher> {

    /**
     * Retrieves a Teacher object by its name.
     *
     * @param name The name of the Teacher to retrieve.
     * @return The Teacher object with the specified name, or null if not found.
     * @throws RepositoryOperationException If an error occurs during the repository operation.
     * @throws ValidationException If the validation of the Teacher name fails.
     */
    public Teacher getByName(String name) throws RepositoryOperationException, ValidationException;

    /**
     * Creates a new Teacher object with the specified name and type.
     *
     * @param name The name of the new Teacher.
     * @param type The type of the new Teacher.
     * @return The newly created Teacher object.
     * @throws RepositoryOperationException If an error occurs during the repository operation.
     * @throws ValidationException If the validation of the Teacher attributes fails.
     */
    public Teacher createNewTeacher(String name, Teacher.Type type) throws RepositoryOperationException, ValidationException;
}
