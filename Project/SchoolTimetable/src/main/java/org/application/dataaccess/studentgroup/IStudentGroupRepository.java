package org.application.dataaccess.studentgroup;

import org.application.dataaccess.repository.IRepository;
import org.application.domain.exceptions.RepositoryOperationException;
import org.application.domain.models.StudentGroup;

public interface IStudentGroupRepository extends IRepository<StudentGroup> {

    public StudentGroup getByGroupName(String groupName);
    public StudentGroup createNewGroup(String groupName) throws RepositoryOperationException;
}
