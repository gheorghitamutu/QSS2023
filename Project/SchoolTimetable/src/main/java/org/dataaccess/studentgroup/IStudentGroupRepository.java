package org.dataaccess.studentgroup;

import org.dataaccess.repository.IRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.StudentGroup;

public interface IStudentGroupRepository extends IRepository<StudentGroup> {

    public StudentGroup getByGroupName(String groupName) throws RepositoryOperationException, ValidationException;
    public StudentGroup createNewGroup(String groupName) throws RepositoryOperationException, ValidationException;
}
