package org.dataaccess.teacher;

import org.dataaccess.repository.IRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.models.Teacher;

public interface ITeacherRepository extends IRepository<Teacher> {
    public Teacher getByName(String name) throws RepositoryOperationException;
    public Teacher createNewTeacher(String name, Teacher.Type type) throws RepositoryOperationException;
}
