package org.application.studentgroups;

import org.domain.exceptions.studentgroup.StudentGroupAdditionException;
import org.domain.exceptions.studentgroup.StudentGroupDeletionFailed;
import org.domain.exceptions.studentgroup.StudentGroupNotFoundException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.StudentGroup;

import java.util.List;

/**
 * Represents a service interface for managing StudentGroup entities.
 * This interface defines the service operations that can be performed, specific to student group service.
 */
public interface IStudentGroupsService {

    /**
     * Adds a new StudentGroup with the given name, year and type.
     * @param name The name of the new StudentGroup.
     * @param year The year of the new StudentGroup.
     * @param type The type of the new StudentGroup.
     * @return The newly created StudentGroup.
     * @throws StudentGroupAdditionException If the StudentGroup could not be added.
     * @throws ValidationException If the provided name, year or type are invalid.
     */
    public StudentGroup addStudentGroup(String name, int year, StudentGroup.Type type) throws StudentGroupAdditionException, ValidationException;

    /**
     * Deletes a StudentGroup with the given id.
     * @param studentGroupId The id of the StudentGroup to delete.
     * @return True if the StudentGroup was deleted, false otherwise.
     * @throws StudentGroupNotFoundException If the StudentGroup with the given id was not found.
     * @throws StudentGroupDeletionFailed If the StudentGroup could not be deleted.
     * @throws ValidationException If the provided id is invalid.
     */
    public boolean deleteStudentGroup(int studentGroupId) throws StudentGroupNotFoundException, StudentGroupDeletionFailed, ValidationException;

    /**
     * Deletes a StudentGroup with the given name.
     * @param name The name of the StudentGroup to delete.
     * @return True if the StudentGroup was deleted, false otherwise.
     * @throws StudentGroupDeletionFailed If the StudentGroup could not be deleted.
     * @throws ValidationException If the provided name is invalid.
     */
    public boolean deleteStudentGroup(String name) throws StudentGroupDeletionFailed, ValidationException;

    /**
     * Deletes all StudentGroup entities.
     * @return True if all StudentGroup entities were deleted, false otherwise.
     * @throws StudentGroupDeletionFailed If the StudentGroup entities could not be deleted.
     */
    public boolean deleteAll() throws StudentGroupDeletionFailed;

    /**
     * Retrieves a StudentGroup by its ID.
     * @param studentGroupId The id of the StudentGroup to retrieve.
     * @return The StudentGroup with the given id.
     * @throws StudentGroupNotFoundException If the StudentGroup with the given id was not found.
     * @throws ValidationException If the provided id is invalid.
     */
    public StudentGroup getStudentGroupById(int studentGroupId) throws StudentGroupNotFoundException, ValidationException;

    /**
     * Retrieves all StudentGroup entities.
     * @return A list of all StudentGroup entities.
     */
    public List<StudentGroup> getStudentGroups();

    /**
     * Retrieves all StudentGroup entities with the given year.
     * @param year The year of the StudentGroup entities to retrieve.
     * @return A list of all StudentGroup entities with the given year.
     */
    public List<StudentGroup> getStudentGroupsByYear(int year);

    /**
     * Retrieves all StudentGroup entities with the given type.
     * @param type The type of the StudentGroup entities to retrieve.
     * @return A list of all StudentGroup entities with the given type.
     */
    public List<StudentGroup> getStudentGroupsByType(StudentGroup.Type type);
}
