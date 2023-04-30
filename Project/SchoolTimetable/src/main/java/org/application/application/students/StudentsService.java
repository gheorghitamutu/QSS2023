package org.application.application.students;

import com.google.inject.Inject;
import org.application.dataaccess.student.IStudentRepository;
import org.application.dataaccess.studentgroup.IStudentGroupRepository;
import org.application.domain.exceptions.*;
import org.application.domain.models.Student;
import org.application.domain.models.StudentGroup;

import java.util.Date;
import java.util.List;

public class StudentsService implements IStudentsService {

    private final IStudentRepository studentRepository;
    private final IStudentGroupRepository studentGroupRepository;

    @Inject

    public StudentsService(IStudentRepository studentRepository,
                           IStudentGroupRepository studentGroupRepository) {

        this.studentRepository = studentRepository;
        this.studentGroupRepository = studentGroupRepository;
    }

    @Override
    public Student addStudent(String name, int year, String groupName) throws StudentAdditionException {
        var student = new Student();

        student.setName(name);
        student.setYear(year);
        student.setInsertTime(new Date());


        StudentGroup newStudentGroup = studentGroupRepository.getByGroupName(groupName);

        if (newStudentGroup == null)
        {
            try {
                System.out.println("[Students Service]  Creating new group with name" + groupName + ".");

                newStudentGroup = studentGroupRepository.createNewGroup(groupName);

                System.out.println("[Students Service]  Created new group with name" + groupName + ".");

            } catch (RepositoryOperationException e) {
                System.out.println("[Students Service]  Couldn't create new group.");
                throw new RuntimeException(e);
            }
        }
        student.setGroup(newStudentGroup);

        try {
            studentRepository.save(student);
            return student;
        } catch (Exception e) {
            throw new StudentAdditionException("[Students Service]  Couldn't add student.",e);
        }
    }

    @Override
    public Student updateStudent(int studentId, String name, int year) throws StudentUpdateException {
        var student = studentRepository.getById(studentId);

        student.setName(name);
        student.setYear(year);

        try {
            studentRepository.updateStudent(student);
            return student;
        } catch (Exception e) {

            throw new StudentUpdateException("[Students Service] Couldn't update student.",e);
        }
    }

    @Override
    public Student reassignStudent(int studentId, String newGroupName) throws StudentGroupReassignException {
        var student = studentRepository.getById(studentId);

        StudentGroup newStudentGroup = studentGroupRepository.getByGroupName(newGroupName);

        if (newStudentGroup == null)
        {
            StudentGroup studentGroup = null;
            try {
                studentGroup = studentGroupRepository.createNewGroup(newGroupName);
            } catch (RepositoryOperationException e) {
                throw new StudentGroupReassignException("[Students Service] Couldn't reassign student to group " + newStudentGroup + ".", e);
            }

            student.setGroup(studentGroup);

            return student;
        }

        student.setGroup(newStudentGroup);

        return student;
    }

    @Override
    public Student getStudentById(int studentId) {
        return studentRepository.getById(studentId);
    }

    @Override
    public boolean deleteStudent(int studentId) throws StudentNotFoundException, StudentDeletionFailed {
        var student = studentRepository.getById(studentId);

        if (student == null)
        {
            throw new StudentNotFoundException("[Students Service DELETE student] Student with id " + studentId + " not found.");
        }

        try {
            studentRepository.deleteStudent(student);

            return true;
        } catch (Exception e) {

            throw new StudentDeletionFailed(" [Students Service] Couldn't delete student.", e);
        }
    }

    @Override
    public List<Student> getStudents() {
        return studentRepository.readAll();
    }
}
