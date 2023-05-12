package org.dataaccess.studentgroup;

import org.dataaccess.repository.IRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.models.StudentGroup;

public interface IStudentGroupRepository extends IRepository<StudentGroup> {

    public StudentGroup getByGroupName(String groupName);
    public StudentGroup createNewGroup(String groupName) throws RepositoryOperationException;
}
