package org.application.studentgroups;

import org.domain.exceptions.studentgroup.StudentGroupAdditionException;
import org.domain.exceptions.studentgroup.StudentGroupDeletionFailed;
import org.domain.exceptions.studentgroup.StudentGroupNotFoundException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.StudentGroup;

import java.util.List;

public interface IStudentGroupsService {

    public StudentGroup addStudentGroup(String name, int year, StudentGroup.Type type) throws StudentGroupAdditionException, ValidationException;

    public boolean deleteStudentGroup(int studentGroupId) throws StudentGroupNotFoundException, StudentGroupDeletionFailed, ValidationException;

    public boolean deleteStudentGroup(String name) throws StudentGroupDeletionFailed, ValidationException;

    public boolean deleteAll() throws StudentGroupDeletionFailed;

    public StudentGroup getStudentGroupById(int studentGroupId) throws StudentGroupNotFoundException, ValidationException;

    public List<StudentGroup> getStudentGroups();

    public List<StudentGroup> getStudentGroupsByYear(int year);

    public List<StudentGroup> getStudentGroupsByType(StudentGroup.Type type);
}
