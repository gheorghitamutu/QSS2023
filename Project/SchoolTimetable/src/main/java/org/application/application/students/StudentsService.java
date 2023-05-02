package org.application.application.students;

import com.google.inject.Inject;
import org.application.dataaccess.student.IStudentRepository;
import org.application.dataaccess.studentgroup.IStudentGroupRepository;
import org.application.domain.exceptions.*;
import org.application.domain.exceptions.student.StudentAdditionException;
import org.application.domain.exceptions.student.StudentDeletionFailed;
import org.application.domain.exceptions.student.StudentNotFoundException;
import org.application.domain.exceptions.student.StudentUpdateException;
import org.application.domain.exceptions.studentgroup.StudentGroupReassignException;
import org.application.domain.models.Student;
import org.application.domain.models.StudentGroup;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentsService implements IStudentsService {

    private final IStudentRepository studentRepository;
    private final IStudentGroupRepository studentGroupRepository;

    @Inject

    public StudentsService(IStudentRepository studentRepository, IStudentGroupRepository studentGroupRepository) {
        this.studentRepository = studentRepository;
        this.studentGroupRepository = studentGroupRepository;
    }

    @Override
    public Student addStudent(String name, int year, String groupName) throws StudentAdditionException {
        var student = new Student();

        student.setName(name);
        student.setYear(year);
        student.setInsertTime(new Date());

        var group = studentGroupRepository.getByGroupName(groupName);
        if (group == null) {
            try {
                System.out.println(MessageFormat.format("[Students Service] Creating new group with name {0}.", groupName));

                group = studentGroupRepository.createNewGroup(groupName);

                System.out.println(MessageFormat.format("[Students Service] Created new group with name {0}.", groupName));

            } catch (RepositoryOperationException e) {
                System.out.println("[Students Service] Couldn't create new group.");
                throw new RuntimeException(e);
            }
        }
        student.setGroup(group);

        try {
            studentRepository.save(student);
        } catch (Exception e) {
            throw new StudentAdditionException("[Students Service] Couldn't add student.", e);
        }

        return student;
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

            throw new StudentUpdateException("[Students Service] Couldn't update student.", e);
        }
    }

    @Override
    public Student reassignStudent(int studentId, String newGroupName) throws StudentGroupReassignException {
        var student = studentRepository.getById(studentId);

        StudentGroup group = studentGroupRepository.getByGroupName(newGroupName);

        if (group == null) {
            try {
                group = studentGroupRepository.createNewGroup(newGroupName);
            } catch (RepositoryOperationException e) {
                throw new StudentGroupReassignException(MessageFormat.format("[Students Service] Couldn't reassign student to group {0}.", newGroupName), e);
            }
        }

        student.setGroup(group);

        return student;
    }

    @Override
    public Student getStudentById(int studentId) throws StudentNotFoundException {
        var student = studentRepository.getById(studentId);
        if (student == null) {
            throw new StudentNotFoundException(MessageFormat.format("[Students Service DELETE student] Student with id {0} not found.", studentId));
        }

        return studentRepository.getById(studentId);
    }

    public Student getStudentByRegistrationNumber(String registrationNumber) throws StudentNotFoundException {
        var students = studentRepository.readAll().stream().filter(s -> s.getRegistrationNumber().equals(registrationNumber)).toList();
        if (students.size() != 1) {
            throw new StudentNotFoundException(MessageFormat.format("[Students Service DELETE student] Student with id {0} not found.", registrationNumber));
        }
        return students.get(0);
    }

    @Override
    public boolean deleteStudent(int studentId) throws StudentNotFoundException, StudentDeletionFailed {
        var student = studentRepository.getById(studentId);

        if (student == null) {
            throw new StudentNotFoundException(MessageFormat.format("[Students Service DELETE student] Student with id {0} not found.", studentId));
        }

        try {
            studentRepository.deleteStudent(student);
        } catch (Exception e) {
            throw new StudentDeletionFailed(" [Students Service] Couldn't delete student.", e);
        }

        return true;
    }

    @Override
    public boolean deleteStudent(String registrationNumber) throws StudentNotFoundException, StudentDeletionFailed {
        var students = studentRepository.readAll().stream().filter(s -> s.getRegistrationNumber().equals(registrationNumber)).toList();
        if (students.size() != 1) {
            throw new StudentNotFoundException(MessageFormat.format("[Students Service DELETE student] Student with id {0} not found.", registrationNumber));
        }

        var student = students.get(0);
        try {
            studentRepository.deleteStudent(student);
        } catch (Exception e) {
            throw new StudentDeletionFailed(" [Students Service] Couldn't delete student.", e);
        }

        return true;
    }

    @Override
    public List<Student> getStudents() {
        return studentRepository.readAll();
    }

    @Override
    public List<Student> getStudentsByGroupNameAndYear(String groupName, int year) {
        var groups = studentGroupRepository.readAll().stream().filter(group -> group.getName().equals(groupName) && group.getYear() == year).toList();
        List<Student> students = new ArrayList<>();
        for (var group : groups) {
            students.addAll(group.getStudents());
        }

        return students;
    }
}
