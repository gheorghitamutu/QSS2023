package org.dataaccess.student;

import org.dataaccess.repository.IRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.models.Student;
import org.domain.models.StudentGroup;

public interface IStudentRepository extends IRepository<Student> {

    public Student updateStudent(Student student) throws RepositoryOperationException;

    public Student deleteStudent(Student student) throws RepositoryOperationException;
    public Student changeStudentGroup(Student student, StudentGroup newGroup) throws RepositoryOperationException;
}
