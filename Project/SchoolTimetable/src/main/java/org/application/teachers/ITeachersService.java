package org.application.teachers;

import org.domain.exceptions.teacher.TeacherAdditionException;
import org.domain.exceptions.teacher.TeacherDeletionFailed;
import org.domain.exceptions.teacher.TeacherNotFoundException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Teacher;

import java.util.List;

/**
 * Represents a service interface for managing Teacher entities.
 * This interface defines the service operations that can be performed, specific to teacher service.
 */
public interface ITeachersService {

    /**
     * Adds a new Teacher with the given name and type.
     * @param name The name of the new Teacher.
     * @param type The type of the new Teacher.
     * @return The newly created Teacher.
     * @throws TeacherAdditionException If the Teacher could not be added.
     * @throws ValidationException If the provided name or type are invalid.
     */
    public Teacher addTeacher(String name, Teacher.Type type) throws TeacherAdditionException, ValidationException;

    /**
     * Deletes a Teacher with the given id.
     * @param teacherId The id of the Teacher to delete.
     * @return True if the Teacher was deleted, false otherwise.
     * @throws TeacherNotFoundException If the Teacher with the given id was not found.
     * @throws TeacherDeletionFailed If the Teacher could not be deleted.
     * @throws ValidationException If the provided id is invalid.
     */
    public boolean deleteTeacher(int teacherId) throws TeacherNotFoundException, TeacherDeletionFailed, ValidationException;

    /**
     * Deletes a Teacher with the given name.
     * @param name The name of the Teacher to delete.
     * @return True if the Teacher was deleted, false otherwise.
     * @throws TeacherDeletionFailed If the Teacher could not be deleted.
     * @throws ValidationException If the provided name is invalid.
     */
    public boolean deleteTeachers(String name) throws TeacherDeletionFailed, ValidationException;

    /**
     * Deletes all Teacher entities.
     * @return True if all Teacher entities were deleted, false otherwise.
     * @throws TeacherDeletionFailed If the Teacher entities could not be deleted.
     */
    public boolean deleteAll() throws TeacherDeletionFailed;

    /**
     * Retrieves a Teacher by its ID.
     * @param teacherId The id of the Teacher to retrieve.
     * @return The Teacher with the given id.
     * @throws TeacherNotFoundException If the Teacher with the given id was not found.
     * @throws ValidationException If the provided id is invalid.
     */
    public Teacher getTeacherById(int teacherId) throws TeacherNotFoundException, ValidationException;

    /**
     * Retrieves all Teacher entities.
     * @return A list of all Teacher entities.
     */
    public List<Teacher> getTeachers();
}
