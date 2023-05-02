package org.application.application.studentgroups;

import org.application.domain.exceptions.studentgroup.StudentGroupAdditionException;
import org.application.domain.exceptions.studentgroup.StudentGroupDeletionFailed;
import org.application.domain.exceptions.studentgroup.StudentGroupNotFoundException;
import org.application.domain.models.StudentGroup;

import java.util.List;

public interface IStudentGroupsService {

    public StudentGroup addStudentGroup(String name, int year, StudentGroup.Type type) throws StudentGroupAdditionException;

    public boolean deleteStudentGroup(int studentGroupId) throws StudentGroupNotFoundException, StudentGroupDeletionFailed;

    public boolean deleteAll() throws StudentGroupDeletionFailed;

    public StudentGroup getStudentGroupById(int studentGroupId) throws StudentGroupNotFoundException;

    public List<StudentGroup> getStudentGroups();

    public List<StudentGroup> getStudentGroupsByYear(int year);

    public List<StudentGroup> getStudentGroupsByType(StudentGroup.Type type);
}
