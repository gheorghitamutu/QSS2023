package org.application.dataaccess.student;

import org.application.dataaccess.repository.IRepository;
import org.application.domain.exceptions.RepositoryOperationException;
import org.application.domain.models.Student;
import org.application.domain.models.StudentGroup;

public interface IStudentRepository extends IRepository<Student> {

    public Student updateStudent(Student student) throws RepositoryOperationException;

    public Student deleteStudent(Student student) throws RepositoryOperationException;
    public Student changeStudentGroup(Student student, StudentGroup newGroup) throws RepositoryOperationException;
}
