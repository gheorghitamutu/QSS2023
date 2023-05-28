package org.dataaccess.student;

import org.dataaccess.repository.IRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Student;
import org.domain.models.StudentGroup;

/**
 * An interface for a repository that manages Student entities.
 * Extends the IRepository interface with Student as the generic type.
 * This interface defines the repository operations that can be performed, specific to student repository.
 */
public interface IStudentRepository extends IRepository<Student> {

    /**
     * Updates the information of a Student entity.
     *
     * @param student The Student entity to update.
     * @return The updated Student entity.
     * @throws RepositoryOperationException If an error occurs during the repository operation.
     * @throws ValidationException If the validation of the Student attributes fails.
     */
    public Student updateStudent(Student student) throws RepositoryOperationException, ValidationException;

    /**
     * Deletes a Student entity from the repository.
     *
     * @param student The Student entity to delete.
     * @return The deleted Student entity.
     * @throws RepositoryOperationException If an error occurs during the repository operation.
     * @throws ValidationException If the validation of the Student attributes fails.
     */
    public Student deleteStudent(Student student) throws RepositoryOperationException, ValidationException;

    /**
     * Changes the StudentGroup of a Student object.
     *
     * @param student   The Student entity for which to change the group.
     * @param newGroup  The new StudentGroup for the Student entity.
     * @return The updated Student entity with the new group.
     * @throws RepositoryOperationException If an error occurs during the repository operation.
     * @throws ValidationException If the validation of the Student attributes fails.
     */
    public Student changeStudentGroup(Student student, StudentGroup newGroup) throws RepositoryOperationException, ValidationException;
}
