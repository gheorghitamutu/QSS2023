package org.application.studentgroups;

import com.google.inject.Inject;
import org.application.helpers.ValidationHelpers;
import org.dataaccess.studentgroup.IStudentGroupRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.studentgroup.StudentGroupAdditionException;
import org.domain.exceptions.studentgroup.StudentGroupDeletionFailed;
import org.domain.exceptions.studentgroup.StudentGroupNotFoundException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.StudentGroup;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

/**
 * This class is the implementation of the IStudentGroupsService interface.
 */
public class StudentGroupsService implements IStudentGroupsService {

    /**
     * The student group repository.
     */
    private final IStudentGroupRepository studentGroupRepository;

    /**
     * This method initializes the student group repository.
     * @param studentGroupRepository The student group repository.
     */
    @Inject
    public StudentGroupsService(IStudentGroupRepository studentGroupRepository) {
        this.studentGroupRepository = studentGroupRepository;
    }

    /**
     * This method adds a student group.
     * @param name The name of the group.
     * @param year The year of the group.
     * @param type The type of the group.
     * @return The added student group.
     * @throws StudentGroupAdditionException Thrown if the student group could not be added.
     * @throws ValidationException Thrown if the parameters are invalid.
     */
    @Override
    public StudentGroup addStudentGroup(String name, int year, StudentGroup.Type type) throws StudentGroupAdditionException, ValidationException {

        ValidationHelpers.requireNotBlank(name, IllegalArgumentException.class, "[StudentGroups Service] Group name is invalid", null);
        ValidationHelpers.requirePositive(year, IllegalArgumentException.class, "[StudentGroups Service] Group year is invalid", null);
        ValidationHelpers.requireNotNull(type, IllegalArgumentException.class, "[StudentGroups Service] Group type is invalid", null);

        StudentGroup group = null;
        try {
            group = studentGroupRepository.getByGroupName(name);
        } catch (RepositoryOperationException e) {
            throw new StudentGroupAdditionException("Group name is invalid", e);
        } catch (ValidationException e) {
            throw new IllegalArgumentException("Some parameters are invalid", e);
        }

        if (group == null) {
            group = new StudentGroup();
            group.setName(name);
            group.setYear(1);
            group.setType(StudentGroup.Type.BACHELOR);
            group.setInsertTime(new Date());

            try {
                group = studentGroupRepository.createNewGroup(name);
            } catch (RepositoryOperationException | ValidationException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            studentGroupRepository.save(group);
        } catch (Exception e) {
            throw new StudentGroupAdditionException("[StudentGroupsService] Failed adding group!", e);
        }

        return group;
    }

    /**
     * Deletes a student group by id.
     * @param studentGroupId The id of the StudentGroup to delete.
     * @return True if the group was deleted, false otherwise.
     * @throws StudentGroupNotFoundException Thrown if the student group was not found.
     * @throws StudentGroupDeletionFailed Thrown if the student group could not be deleted.
     * @throws ValidationException Thrown if the parameters are invalid.
     */
    @Override
    public boolean deleteStudentGroup(int studentGroupId) throws StudentGroupNotFoundException, StudentGroupDeletionFailed, ValidationException {

        ValidationHelpers.requirePositiveOrZero(studentGroupId, IllegalArgumentException.class, "[StudentGroups Service] Group id is invalid", null);

        var group = studentGroupRepository.getById(studentGroupId);
        if (group == null) {
            throw new StudentGroupNotFoundException(MessageFormat.format("[StudentGroupsService] Student Group with id {0} not found.", studentGroupId));
        }

        try {
            studentGroupRepository.delete(group);
        } catch (Exception e) {
            throw new StudentGroupDeletionFailed(" [StudentGroupsService] Failed to delete student group.", e);
        }

        return true;
    }

    /**
     * Deletes a student group by name.
     * @param name The name of the StudentGroup to delete.
     * @return True if the group was deleted, false otherwise.
     * @throws StudentGroupDeletionFailed Thrown if the student group could not be deleted.
     * @throws ValidationException Thrown if the parameters are invalid.
     */
    @Override
    public boolean deleteStudentGroup(String name) throws StudentGroupDeletionFailed, ValidationException {

        ValidationHelpers.requireNotBlank(name, IllegalArgumentException.class, "[StudentGroups Service] Group name is invalid", null);

        var groups = studentGroupRepository.readAll().stream().filter(sg -> sg.getName().equals(name)).toList();

        try {
            studentGroupRepository.deleteMany(groups);
        } catch (Exception e) {
            throw new StudentGroupDeletionFailed(" [StudentGroupsService] Failed to delete student groups.", e);
        }

        return true;
    }

    /**
     * Deletes all student groups.
     * @return True if the groups were deleted, false otherwise.
     * @throws StudentGroupDeletionFailed Thrown if the student groups could not be deleted.
     */
    @Override
    public boolean deleteAll() throws StudentGroupDeletionFailed {

        try {
            studentGroupRepository.deleteMany(studentGroupRepository.readAll());
        } catch (Exception e) {
            throw new StudentGroupDeletionFailed(" [StudentGroupsService] Failed to delete student groups.", e);
        }

        return true;
    }

    /**
     * Gets a student group by id.
     * @param studentGroupId The id of the StudentGroup to get.
     * @return The StudentGroup with the given id.
     * @throws StudentGroupNotFoundException Thrown if the student group was not found.
     * @throws ValidationException Thrown if the parameters are invalid.
     */
    @Override
    public StudentGroup getStudentGroupById(int studentGroupId) throws StudentGroupNotFoundException, ValidationException {
        ValidationHelpers.requirePositiveOrZero(studentGroupId, IllegalArgumentException.class, "[StudentGroups Service] Group id is invalid", null);

        var group = studentGroupRepository.getById(studentGroupId);
        if (group == null) {
            throw new StudentGroupNotFoundException(MessageFormat.format("[StudentGroupsService] Student Group with id {0} not found.", studentGroupId));
        }
        return group;
    }

    /**
     * Retrieves all student groups.
     * @return A list of all student groups.
     */
    @Override
    public List<StudentGroup> getStudentGroups() {
        return studentGroupRepository.readAll();
    }

    /**
     * Retrieves all student groups by year.
     * @param year The year of the StudentGroup entities to retrieve.
     * @return A list of all student groups with the given year.
     */
    @Override
    public List<StudentGroup> getStudentGroupsByYear(int year) {
        return studentGroupRepository.readAll().stream().filter(group -> group.getYear() == year).toList();
    }

    /**
     * Retrieves all student groups by type.
     * @param type The type of the StudentGroup entities to retrieve.
     * @return A list of all student groups with the given type.
     */
    @Override
    public List<StudentGroup> getStudentGroupsByType(StudentGroup.Type type) {
        return studentGroupRepository.readAll().stream().filter(group -> group.getType() == type).toList();
    }
}
