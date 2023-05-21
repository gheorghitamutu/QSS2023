package org.application.studentgroups;

import com.google.inject.Inject;
import org.dataaccess.studentgroup.IStudentGroupRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.studentgroup.StudentGroupAdditionException;
import org.domain.exceptions.studentgroup.StudentGroupDeletionFailed;
import org.domain.exceptions.studentgroup.StudentGroupNotFoundException;
import org.domain.models.StudentGroup;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

public class StudentGroupsService implements IStudentGroupsService {

    private final IStudentGroupRepository studentGroupRepository;

    @Inject
    public StudentGroupsService(IStudentGroupRepository studentGroupRepository) {
        this.studentGroupRepository = studentGroupRepository;
    }

    @Override
    public StudentGroup addStudentGroup(String name, int year, StudentGroup.Type type) throws StudentGroupAdditionException {

        if (name == null || name.isEmpty()) {
            throw new StudentGroupAdditionException("[StudentGroups Service] Group name is invalid");
        }

        if (year <= 0) {
            throw new StudentGroupAdditionException("[StudentGroups Service] Group year is invalid");
        }

        if (type == null) {
            throw new StudentGroupAdditionException("[StudentGroups Service] Group type is invalid");
        }

        StudentGroup group = null;
        try {
            group = studentGroupRepository.getByGroupName(name);
        } catch (RepositoryOperationException e) {
            throw new StudentGroupAdditionException("Group name is invalid", e);
        }

        if (group == null) {
            group = new StudentGroup();
            group.setName(name);
            group.setYear(1);
            group.setType(StudentGroup.Type.BACHELOR);
            group.setInsertTime(new Date());

            try {
                group = studentGroupRepository.createNewGroup(name);
            } catch (RepositoryOperationException e) {
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

    @Override
    public boolean deleteStudentGroup(int studentGroupId) throws StudentGroupNotFoundException, StudentGroupDeletionFailed {

        if (studentGroupId < 0) {
            throw new IllegalArgumentException("[StudentGroups Service] Group id is invalid");
        }

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

    @Override
    public boolean deleteStudentGroup(String name) throws StudentGroupDeletionFailed {

        if (name == null || name.isEmpty()) {
            throw new StudentGroupDeletionFailed("[StudentGroups Service] Group name is null");
        }

        var groups = studentGroupRepository.readAll().stream().filter(sg -> sg.getName().equals(name)).toList();

        try {
            studentGroupRepository.deleteMany(groups);
        } catch (Exception e) {
            throw new StudentGroupDeletionFailed(" [StudentGroupsService] Failed to delete student groups.", e);
        }

        return true;
    }

    @Override
    public boolean deleteAll() throws StudentGroupDeletionFailed {

        try {
            studentGroupRepository.deleteMany(studentGroupRepository.readAll());
        } catch (Exception e) {
            throw new StudentGroupDeletionFailed(" [StudentGroupsService] Failed to delete student groups.", e);
        }

        return true;
    }

    @Override
    public StudentGroup getStudentGroupById(int studentGroupId) throws StudentGroupNotFoundException {
        if (studentGroupId < 0) {
            throw new IllegalArgumentException("[StudentGroups Service] Group id is invalid");
        }

        var group = studentGroupRepository.getById(studentGroupId);
        if (group == null) {
            throw new StudentGroupNotFoundException(MessageFormat.format("[StudentGroupsService] Student Group with id {0} not found.", studentGroupId));
        }
        return group;
    }

    @Override
    public List<StudentGroup> getStudentGroups() {
        return studentGroupRepository.readAll();
    }

    @Override
    public List<StudentGroup> getStudentGroupsByYear(int year) {
        return studentGroupRepository.readAll().stream().filter(group -> group.getYear() == year).toList();
    }

    @Override
    public List<StudentGroup> getStudentGroupsByType(StudentGroup.Type type) {
        return studentGroupRepository.readAll().stream().filter(group -> group.getType() == type).toList();
    }
}
