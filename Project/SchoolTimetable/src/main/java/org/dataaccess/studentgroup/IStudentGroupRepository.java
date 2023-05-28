package org.dataaccess.studentgroup;

import org.dataaccess.repository.IRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.StudentGroup;

/**
 * This is the interface for StudentGroup.
 */
public interface IStudentGroupRepository extends IRepository<StudentGroup> {

    /**
     * This is the method to get a student group by group name.
     * @param groupName The group name.
     * @return The student group.
     * @throws RepositoryOperationException The repository operation exception.
     * @throws ValidationException The validation exception.
     */
    public StudentGroup getByGroupName(String groupName) throws RepositoryOperationException, ValidationException;

    /**
     * This is the method to create a new student group.
     * @param groupName The group name.
     * @return The student group.
     * @throws RepositoryOperationException The repository operation exception.
     * @throws ValidationException The validation exception.
     */
    public StudentGroup createNewGroup(String groupName) throws RepositoryOperationException, ValidationException;
}
