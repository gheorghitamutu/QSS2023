package org.dataaccess.student;

import org.dataaccess.repository.IRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Student;
import org.domain.models.StudentGroup;

/**
 * This is the interface for Student.
 */
public interface IStudentRepository extends IRepository<Student> {

    /**
     * This is the method to update a student.
     * @param student The student.
     * @return The updated student.
     * @throws RepositoryOperationException The repository operation exception.
     * @throws ValidationException The validation exception.
     */
    public Student updateStudent(Student student) throws RepositoryOperationException, ValidationException;

    /**
     * This is the method to delete a student.
     * @param student The student.
     * @return The student.
     * @throws RepositoryOperationException The repository operation exception.
     * @throws ValidationException The validation exception.
     */
    public Student deleteStudent(Student student) throws RepositoryOperationException, ValidationException;

    /**
     * This is the method to change a student group.
     * @param student The student.
     * @param newGroup The new group.
     * @return The student.
     * @throws RepositoryOperationException The repository operation exception.
     * @throws ValidationException The validation exception.
     */
    public Student changeStudentGroup(Student student, StudentGroup newGroup) throws RepositoryOperationException, ValidationException;
}
