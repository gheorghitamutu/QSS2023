package org.application.students;

import com.google.inject.Inject;
import org.dataaccess.student.IStudentRepository;
import org.dataaccess.studentgroup.IStudentGroupRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.student.StudentAdditionException;
import org.domain.exceptions.student.StudentDeletionFailed;
import org.domain.exceptions.student.StudentNotFoundException;
import org.domain.exceptions.student.StudentUpdateException;
import org.domain.exceptions.studentgroup.StudentGroupReassignException;
import org.domain.models.Student;
import org.domain.models.StudentGroup;

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
    public Student addStudent(String name, String registrationNumber, int year, String groupName) throws StudentAdditionException {
        var student = new Student();

        student.setName(name);
        student.setYear(year);
        student.setRegistrationNumber(registrationNumber);
        student.setInsertTime(new Date());

        var group = studentGroupRepository.getByGroupName(groupName);
        if (group == null) {
            try {
                System.out.println(MessageFormat.format("[StudentsService] Creating new group with name {0}.", groupName));

                group = studentGroupRepository.createNewGroup(groupName);

                System.out.println(MessageFormat.format("[StudentsService] Created new group with name {0}.", groupName));

            } catch (RepositoryOperationException e) {
                throw new StudentAdditionException("[StudentsService] Couldn't create new group for adding the student (group doesnt exist yet).", e);
            }
        }
        student.setGroup(group);

        try {
            studentRepository.save(student);
        } catch (Exception e) {
            throw new StudentAdditionException("[StudentsService] Couldn't add student.", e);
        }

        return student;
    }

    @Override
    public Student updateStudent(int studentId, String name, int year) throws StudentUpdateException {
        var student = studentRepository.getById(studentId);

        if (student == null) {
            throw new StudentUpdateException(MessageFormat.format("[StudentsService] Couldn't update student with id {0}. Student not found.", studentId));
        }

        student.setName(name);
        student.setYear(year);

        try {
            studentRepository.updateStudent(student);
        } catch (Exception e) {
            throw new StudentUpdateException("[StudentsService] Couldn't update student.", e);
        }

        return student;
    }

    @Override
    public Student reassignStudent(int studentId, String newGroupName) throws StudentGroupReassignException, RepositoryOperationException {
        var student = studentRepository.getById(studentId);

        StudentGroup group = studentGroupRepository.getByGroupName(newGroupName);

        if (group == null) {
            try {
                group = studentGroupRepository.createNewGroup(newGroupName);
            } catch (RepositoryOperationException e) {
                throw new StudentGroupReassignException(MessageFormat.format("[StudentsService] Couldn't reassign student to group {0}.", newGroupName), e);
            }
        }

        student.setGroup(group);

        studentGroupRepository.save(group);

        return student;
    }

    @Override
    public Student getStudentById(int studentId) throws StudentNotFoundException {
        var student = studentRepository.getById(studentId);
        if (student == null) {
            throw new StudentNotFoundException(MessageFormat.format("[StudentsService DELETE student] Student with id {0} not found.", studentId));
        }

        return studentRepository.getById(studentId);
    }

    public Student getStudentByRegistrationNumber(String registrationNumber) throws StudentNotFoundException {
        var students = studentRepository.readAll().stream().filter(s -> s.getRegistrationNumber().equals(registrationNumber)).toList();
        if (students.size() != 1) {
            throw new StudentNotFoundException(MessageFormat.format("[StudentsService DELETE student] Student with id {0} not found.", registrationNumber));
        }
        return students.get(0);
    }

    @Override
    public boolean deleteStudent(int studentId) throws StudentNotFoundException, StudentDeletionFailed {
        var student = studentRepository.getById(studentId);

        if (student == null) {
            throw new StudentNotFoundException(MessageFormat.format("[StudentsService DELETE student] Student with id {0} not found.", studentId));
        }

        try {
            studentRepository.deleteStudent(student);
        } catch (Exception e) {
            throw new StudentDeletionFailed(" [StudentsService] Couldn't delete student.", e);
        }

        return true;
    }

    @Override
    public boolean deleteStudents(String registrationNumber) throws StudentNotFoundException, StudentDeletionFailed {
        var students = studentRepository.readAll().stream().filter(s -> s.getRegistrationNumber().equals(registrationNumber)).toList();

        try {
            studentRepository.deleteMany(students);
        } catch (Exception e) {
            throw new StudentDeletionFailed(" [StudentsService] Couldn't delete students.", e);
        }

        return true;
    }

    @Override
    public boolean deleteAll() throws StudentDeletionFailed {
        try {
            studentRepository.deleteMany(studentRepository.readAll());
        } catch (Exception e) {
            throw new StudentDeletionFailed(" [StudentsService] Couldn't delete students.", e);
        }

        return true;
    }

    @Override
    public boolean deleteStudent(String registrationNumber) throws StudentNotFoundException, StudentDeletionFailed {
        var students = studentRepository.readAll().stream().filter(s -> s.getRegistrationNumber().equals(registrationNumber)).toList();
        if (students.size() != 1) {
            throw new StudentNotFoundException(MessageFormat.format("[StudentsService DELETE student] Student with id {0} not found.", registrationNumber));
        }

        var student = students.get(0);
        try {
            studentRepository.deleteStudent(student);
        } catch (Exception e) {
            throw new StudentDeletionFailed(" [StudentsService] Couldn't delete student.", e);
        }

        return true;
    }

    @Override
    public List<Student> getStudents() {
        return studentRepository.readAll();
    }

    @Override
    public List<Student> getStudentsByGroupNameAndYear(String groupName, int year) {

        var allGroups = studentGroupRepository.readAll();

        var groupsFiltered = allGroups.stream().filter(group -> group.getName().equals(groupName) && group.getYear() == year).toList();

        List<Student> foundStudents = new ArrayList<>();
        for (var group : groupsFiltered) {
            foundStudents.addAll(group.getStudents());
        }

        return foundStudents;
    }
}
