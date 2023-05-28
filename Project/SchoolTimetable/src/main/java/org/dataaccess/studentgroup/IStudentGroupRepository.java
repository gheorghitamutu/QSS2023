package org.dataaccess.studentgroup;

import org.dataaccess.repository.IRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.StudentGroup;

/**
 * An interface representing a repository for managing StudentGroup entities.
 * Extends the IRepository interface with StudentGroup as the generic type.
 * This interface defines the repository operations that can be performed, specific to student group repository.
 */
public interface IStudentGroupRepository extends IRepository<StudentGroup> {

    /**
     * Retrieves a StudentGroup object by its group name.
     *
     * @param groupName The name of the group to retrieve.
     * @return The StudentGroup entity with the specified group name, or null if not found.
     * @throws RepositoryOperationException If an error occurs during the repository operation.
     * @throws ValidationException If the validation of the group name fails.
     */
    public StudentGroup getByGroupName(String groupName) throws RepositoryOperationException, ValidationException;

    /**
     * Creates a new StudentGroup entities with the specified group name.
     *
     * @param groupName The name of the new group.
     * @return The newly created StudentGroup entity.
     * @throws RepositoryOperationException If an error occurs during the repository operation.
     * @throws ValidationException If the validation of the group name fails.
     */
    public StudentGroup createNewGroup(String groupName) throws RepositoryOperationException, ValidationException;
}
