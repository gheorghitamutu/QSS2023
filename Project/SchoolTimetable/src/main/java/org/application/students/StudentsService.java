package org.application.students;

import com.google.inject.Inject;
import org.application.helpers.ValidationHelpers;
import org.dataaccess.student.IStudentRepository;
import org.dataaccess.studentgroup.IStudentGroupRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.student.StudentAdditionException;
import org.domain.exceptions.student.StudentDeletionFailed;
import org.domain.exceptions.student.StudentNotFoundException;
import org.domain.exceptions.student.StudentUpdateException;
import org.domain.exceptions.studentgroup.StudentGroupReassignException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Student;
import org.domain.models.StudentGroup;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents a service class for managing Student entities.
 */
public class StudentsService implements IStudentsService {

    /**
     * The student repository.
     */
    private final IStudentRepository studentRepository;

    /**
     * The student group repository.
     */
    private final IStudentGroupRepository studentGroupRepository;

    /**
     * Initializes a new instance of the {@link StudentsService} class.
     * @param studentRepository The student repository.
     * @param studentGroupRepository The student group repository.
     */
    @Inject
    public StudentsService(IStudentRepository studentRepository, IStudentGroupRepository studentGroupRepository) {
        this.studentRepository = studentRepository;
        this.studentGroupRepository = studentGroupRepository;
    }

    /**
     * Adds a new Student with the given name, registration number, year and group name.
     * @param name The name of the new Student.
     * @param registrationNumber The registration number of the new Student.
     * @param year The year of the new Student.
     * @param groupName The group name of the new Student.
     * @return The newly created Student.
     * @throws StudentAdditionException If the Student could not be added.
     * @throws ValidationException If the provided name, registration number, year or group name are invalid.
     */
    @Override
    public Student addStudent(String name, String registrationNumber, int year, String groupName) throws StudentAdditionException, ValidationException {

        ValidationHelpers.requireNotBlank(name, IllegalArgumentException.class, "[Students Service] Student name is invalid", null);
        ValidationHelpers.requireNotBlank(registrationNumber, IllegalArgumentException.class, "[Students Service] Student registration number is invalid", null);
        ValidationHelpers.requirePositive(year, IllegalArgumentException.class, "[Students Service] Student year is invalid", null);
        ValidationHelpers.requireNotBlank(groupName, IllegalArgumentException.class, "[Students Service] Student group name is invalid", null);

        var student = new Student();

        student.setName(name);
        student.setYear(year);
        student.setRegistrationNumber(registrationNumber);
        student.setInsertTime(new Date());

        StudentGroup group = null;
        try {
            group = studentGroupRepository.getByGroupName(groupName);
        } catch (RepositoryOperationException | ValidationException e) {
            throw new StudentAdditionException("[Students Service] (New) Group name is invalid",e);
        }
        if (group == null) {
            try {
                System.out.println(MessageFormat.format("[StudentsService] Creating new group with name {0}.", groupName));

                group = studentGroupRepository.createNewGroup(groupName);

                System.out.println(MessageFormat.format("[StudentsService] Created new group with name {0}.", groupName));

            } catch (RepositoryOperationException | ValidationException e) {
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

    /**
     * Updates the Student with the given id with the given name and year.
     * @param studentId The id of the Student to update.
     * @param name The new name of the Student.
     * @param year The new year of the Student.
     * @return The updated Student.
     * @throws StudentUpdateException If the Student could not be updated.
     * @throws ValidationException If the provided name or year are invalid.
     */
    @Override
    public Student updateStudent(int studentId, String name, int year) throws StudentUpdateException, ValidationException {

        ValidationHelpers.requireNotBlank(name, IllegalArgumentException.class, "[Students Service] Student name is invalid", null);
        ValidationHelpers.requirePositive(year, IllegalArgumentException.class, "[Students Service] Student year is invalid", null);
        ValidationHelpers.requirePositiveOrZero(studentId, IllegalArgumentException.class, "[Students Service] Student id is invalid", null);

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

    /**
     * Reassigns the Student with the given id to the group with the given name.
     * @param studentId The id of the Student to reassign.
     * @param newGroupName The new group name of the Student.
     * @return The reassigned Student.
     * @throws StudentGroupReassignException If the Student could not be reassigned.
     * @throws RepositoryOperationException If the Student could not be reassigned.
     * @throws ValidationException If the provided group name is invalid.
     */
    @Override
    public Student reassignStudent(int studentId, String newGroupName) throws StudentGroupReassignException, RepositoryOperationException, ValidationException {
        var student = studentRepository.getById(studentId);

        StudentGroup group = studentGroupRepository.getByGroupName(newGroupName);

        if (group == null) {
            try {
                group = studentGroupRepository.createNewGroup(newGroupName);
            } catch (RepositoryOperationException | ValidationException e) {
                throw new StudentGroupReassignException(MessageFormat.format("[StudentsService] Couldn't reassign student to group {0}.", newGroupName), e);
            }
        }

        student.setGroup(group);

        studentGroupRepository.save(group);

        return student;
    }

    /**
     * Retrieves student by id.
     * @param studentId The id of the Student to get.
     * @return The Student with the given id.
     * @throws StudentNotFoundException If the Student could not be found.
     */
    @Override
    public Student getStudentById(int studentId) throws StudentNotFoundException {
        var student = studentRepository.getById(studentId);
        if (student == null) {
            throw new StudentNotFoundException(MessageFormat.format("[StudentsService DELETE student] Student with id {0} not found.", studentId));
        }

        return studentRepository.getById(studentId);
    }

    /**
     * Retrieves student by registration number.
     * @param registrationNumber The registration number of the Student to get.
     * @return The Student with the given registration number.
     * @throws StudentNotFoundException If the Student could not be found.
     */
    public Student getStudentByRegistrationNumber(String registrationNumber) throws StudentNotFoundException {
        var students = studentRepository.readAll().stream().filter(s -> s.getRegistrationNumber().equals(registrationNumber)).toList();
        if (students.size() != 1) {
            throw new StudentNotFoundException(MessageFormat.format("[StudentsService DELETE student] Student with id {0} not found.", registrationNumber));
        }
        return students.get(0);
    }

    /**
     * Deletes the Student with the given id.
     * @param studentId The id of the Student to delete.
     * @return True if the Student was deleted, false otherwise.
     * @throws StudentNotFoundException If the Student could not be found.
     * @throws StudentDeletionFailed If the Student could not be deleted.
     */
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

    /**
     * Deletes all Students.
     * @return True if the Students were deleted, false otherwise.
     * @throws StudentDeletionFailed If the Students could not be deleted.
     */
    @Override
    public boolean deleteStudents(String registrationNumber) throws StudentDeletionFailed {
        var students = studentRepository.readAll().stream().filter(s -> s.getRegistrationNumber().equals(registrationNumber)).toList();

        try {
            studentRepository.deleteMany(students);
        } catch (Exception e) {
            throw new StudentDeletionFailed(" [StudentsService] Couldn't delete students.", e);
        }

        return true;
    }

    /**
     * Deletes all Students.
     * @return True if the Students were deleted, false otherwise.
     * @throws StudentDeletionFailed If the Students could not be deleted.
     */
    @Override
    public boolean deleteAll() throws StudentDeletionFailed {
        try {
            studentRepository.deleteMany(studentRepository.readAll());
        } catch (Exception e) {
            throw new StudentDeletionFailed(" [StudentsService] Couldn't delete students.", e);
        }

        return true;
    }

    /**
     * Deletes the Student with the given registration number.
     * @param registrationNumber The registration number of the Student to delete.
     * @return True if the Student was deleted, false otherwise.
     * @throws StudentNotFoundException If the Student could not be found.
     * @throws StudentDeletionFailed If the Student could not be deleted.
     */
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

    /**
     * Retrieves all Students.
     * @return A list of all Students.
     */
    @Override
    public List<Student> getStudents() {
        return studentRepository.readAll();
    }

    /**
     * Retrieves all Students in the group with the given group name and year.
     * @param groupName The name of the group to get the Students from.
     * @param year The year of the group to get the Students from.
     * @return A list of all Students in the group with the given group name and year.
     */
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
