package org.application.application.studentgroups;

import com.google.inject.Inject;
import org.application.dataaccess.session.ISessionRepository;
import org.application.dataaccess.student.IStudentRepository;
import org.application.dataaccess.studentgroup.IStudentGroupRepository;
import org.application.domain.exceptions.RepositoryOperationException;
import org.application.domain.exceptions.StudentGroupAdditionException;
import org.application.domain.exceptions.StudentGroupDeletionFailed;
import org.application.domain.exceptions.StudentGroupNotFoundException;
import org.application.domain.models.StudentGroup;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

public class StudentGroupsService implements IStudentGroupsService {

    private final IStudentGroupRepository studentGroupRepository;
    private final IStudentRepository studentRepository;
    private final ISessionRepository sessionRepository;

    @Inject

    public StudentGroupsService(IStudentGroupRepository studentGroupRepository, IStudentRepository studentRepository, ISessionRepository sessionRepository) {
        this.studentGroupRepository = studentGroupRepository;
        this.studentRepository = studentRepository;
        this.sessionRepository = sessionRepository;
    }

    @Override
    public StudentGroup addStudentGroup(String name, int year, StudentGroup.Type type) throws StudentGroupAdditionException {
        StudentGroup group = studentGroupRepository.getByGroupName(name);
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
        var group = studentGroupRepository.getById(studentGroupId);
        if (group == null) {
            throw new StudentGroupNotFoundException(MessageFormat.format("[StudentGroupsService] Student Group with id {0} not found.", studentGroupId));
        }

        try {
            studentGroupRepository.delete(group);
        } catch (Exception e) {
            throw new StudentGroupDeletionFailed(" [StudentGroupsService] Couldn't delete student group.", e);
        }

        return true;
    }

    @Override
    public StudentGroup getStudentGroupById(int studentGroupId) throws StudentGroupNotFoundException {
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
