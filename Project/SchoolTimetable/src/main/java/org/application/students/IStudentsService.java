package org.application.students;

import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.student.StudentAdditionException;
import org.domain.exceptions.student.StudentDeletionFailed;
import org.domain.exceptions.student.StudentNotFoundException;
import org.domain.exceptions.student.StudentUpdateException;
import org.domain.exceptions.studentgroup.StudentGroupReassignException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Student;

import java.util.List;

/**
 * Represents a service interface for managing Student entities.
 * This interface defines the service operations that can be performed, specific to student service.
 */
public interface IStudentsService {

    /**
     * Adds a new Student with the given name, registration number, year and group name.
     * @param name The name of the new Student.
     * @param registrationNumber The registration number of the new Student.
     * @param year The year of the new Student.
     * @param groupName The group name of the new Student.
     * @return The newly created Student.
     * @throws StudentAdditionException If the Student could not be added.
     * @throws ValidationException If the provided name, registration number, year or group name are invalid.
     */
    public Student addStudent(String name, String registrationNumber, int year, String groupName) throws StudentAdditionException, ValidationException;

    /**
     * Updates a Student with the given id.
     * @param studentId The id of the Student to update.
     * @param name The new name of the Student.
     * @param year The new year of the Student.
     * @return The updated Student.
     * @throws StudentUpdateException If the Student could not be updated.
     * @throws ValidationException If the provided id, name or year are invalid.
     */
    public Student updateStudent(int studentId, String name, int year) throws StudentUpdateException, ValidationException;

    /**
     * Deletes a Student with the given id.
     * @param studentId The id of the Student to delete.
     * @return True if the Student was deleted, false otherwise.
     * @throws StudentNotFoundException If the Student with the given id was not found.
     * @throws StudentDeletionFailed If the Student could not be deleted.
     */
    public boolean deleteStudent(int studentId) throws StudentNotFoundException, StudentDeletionFailed;

    /**
     * Deletes a Student with the given registration number.
     * @param registrationNumber The registration number of the Student to delete.
     * @return True if the Student was deleted, false otherwise.
     * @throws StudentNotFoundException If the Student with the given registration number was not found.
     * @throws StudentDeletionFailed If the Student could not be deleted.
     */
    public boolean deleteStudents(String registrationNumber) throws StudentNotFoundException, StudentDeletionFailed;

    /**
     * Deletes all Student entities.
     * @return True if all Student entities were deleted, false otherwise.
     * @throws StudentDeletionFailed If the Student entities could not be deleted.
     */
    public boolean deleteAll() throws StudentDeletionFailed;

    /**
     * Deletes a Student with the given registration number.
     * @param registrationNumber The registration number of the Student to delete.
     * @return True if the Student was deleted, false otherwise.
     * @throws StudentNotFoundException If the Student with the given registration number was not found.
     * @throws StudentDeletionFailed If the Student could not be deleted.
     */
    public boolean deleteStudent(String registrationNumber) throws StudentNotFoundException, StudentDeletionFailed;

    /**
     * Reassigns a Student with the given id to a new group.
     * @param studentId The id of the Student to reassign.
     * @param newGroupName The new group name of the Student.
     * @return The reassigned Student.
     * @throws StudentGroupReassignException If the Student could not be reassigned.
     * @throws RepositoryOperationException If the Student could not be reassigned.
     * @throws ValidationException If the provided id or group name are invalid.
     */
    public Student reassignStudent(int studentId, String newGroupName) throws StudentGroupReassignException, RepositoryOperationException, ValidationException;

    /**
     * Gets a Student with the given id.
     * @param studentId The id of the Student to get.
     * @return The Student with the given id.
     * @throws StudentNotFoundException If the Student with the given id was not found.
     */
    public Student getStudentById(int studentId) throws StudentNotFoundException;

    /**
     * Gets a Student with the given registration number.
     * @param registrationNumber The registration number of the Student to get.
     * @return The Student with the given registration number.
     * @throws StudentNotFoundException If the Student with the given registration number was not found.
     */
    public Student getStudentByRegistrationNumber(String registrationNumber) throws StudentNotFoundException;

    /**
     * Gets all Student entities.
     * @return A list of all Student entities.
     */
    public List<Student> getStudents();

    /**
     * Retrieves all Student entities with the given group name and year.
     * @param groupName The group name of the Student entities to retrieve.
     * @param year The year of the Student entities to retrieve.
     * @return A list of Student entities with the given group name and year.
     */
    public List<Student> getStudentsByGroupNameAndYear(String groupName, int year);
}
