package org.application.dataaccess.teacher;

import org.application.dataaccess.repository.IRepository;
import org.application.domain.exceptions.RepositoryOperationException;
import org.application.domain.models.Discipline;
import org.application.domain.models.Teacher;

public interface ITeacherRepository extends IRepository<Teacher> {
    public Teacher getByName(String name) throws RepositoryOperationException;
    public Teacher createNewTeacher(String name, Teacher.Type type) throws RepositoryOperationException;
}
