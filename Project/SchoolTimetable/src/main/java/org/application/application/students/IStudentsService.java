package org.application.application.students;

import org.application.domain.exceptions.*;
import org.application.domain.models.Student;

import java.util.List;

public interface IStudentsService {

    public Student addStudent(String name, int year, String groupName) throws StudentAdditionException;

    public Student updateStudent(int studentId, String name, int year) throws StudentUpdateException;

    public boolean deleteStudent(int studentId) throws StudentNotFoundException, StudentDeletionFailed;

    public Student reassignStudent(int studentId, String newGroupName) throws StudentGroupReassignException;

    public Student getStudentById(int studentId);

    public List<Student> getStudents();

    public List<Student> getStudentsByGroupNameAndYear(String groupName, int year);
}
