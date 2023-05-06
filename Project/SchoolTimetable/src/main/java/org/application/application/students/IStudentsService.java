package org.application.application.students;

import org.application.domain.exceptions.student.StudentAdditionException;
import org.application.domain.exceptions.student.StudentDeletionFailed;
import org.application.domain.exceptions.student.StudentNotFoundException;
import org.application.domain.exceptions.student.StudentUpdateException;
import org.application.domain.exceptions.studentgroup.StudentGroupReassignException;
import org.application.domain.models.Student;

import java.util.List;

public interface IStudentsService {

    public Student addStudent(String name, String registrationNumber, int year, String groupName) throws StudentAdditionException;

    public Student updateStudent(int studentId, String name, int year) throws StudentUpdateException;

    public boolean deleteStudent(int studentId) throws StudentNotFoundException, StudentDeletionFailed;

    public boolean deleteStudents(String registrationNumber) throws StudentNotFoundException, StudentDeletionFailed;

    public boolean deleteAll() throws StudentDeletionFailed;

    public boolean deleteStudent(String registrationNumber) throws StudentNotFoundException, StudentDeletionFailed;

    public Student reassignStudent(int studentId, String newGroupName) throws StudentGroupReassignException;

    public Student getStudentById(int studentId) throws StudentNotFoundException;

    public Student getStudentByRegistrationNumber(String registrationNumber) throws StudentNotFoundException;

    public List<Student> getStudents();

    public List<Student> getStudentsByGroupNameAndYear(String groupName, int year);
}
