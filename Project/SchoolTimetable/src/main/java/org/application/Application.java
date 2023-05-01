package org.application;

import com.google.inject.Inject;
import org.application.application.students.IStudentsService;
import org.application.dataaccess.student.IStudentRepository;
import org.application.domain.exceptions.student.StudentAdditionException;
import org.application.domain.exceptions.student.StudentDeletionFailed;
import org.application.domain.exceptions.student.StudentNotFoundException;
import org.application.domain.exceptions.student.StudentUpdateException;
import org.application.domain.exceptions.studentgroup.StudentGroupReassignException;
import org.application.domain.models.Student;
import org.application.domain.models.StudentGroup;

import java.util.Date;
import java.util.UUID;

public class Application {

    private final IStudentRepository studentsRepository;
    private final IStudentsService studentsService;

    @Inject
    public Application(IStudentRepository studentsRepository, IStudentsService studentsService) {

        this.studentsRepository = studentsRepository;
        this.studentsService = studentsService;
    }

    public void run() {

        showcaseStudentsService();

        showcaseSampleCodeForRepo();
    }


    private void showcaseStudentsService() {
        var allStudents = studentsService.getStudents();

        System.out.println("All students:");
        for (var student : allStudents) {
            System.out.println(student.getName());
        }

        int newStudentId = 0;
        Student specificStudent = null;

        StudentGroup studentGroup = new StudentGroup();
        studentGroup.setName("A6");
        studentGroup.setYear(1);
        studentGroup.setType(StudentGroup.Type.BACHELOR);
        studentGroup.setInsertTime(new Date());
        Student newStudent = new Student();

        newStudent.setGroup(studentGroup);
        newStudent.setYear(1);
        newStudent.setName("test" + UUID.randomUUID());

        System.out.println("Creating Student...");

        try {
            newStudent = studentsService.addStudent(newStudent.getName(), newStudent.getYear(), newStudent.getGroup().getName());

            newStudentId = newStudent.getId();

            System.out.println("Student with id " + newStudentId + " created.");

        } catch (StudentAdditionException e) {
            System.out.println("Student with id " + newStudentId + " couldn't be created.");

            throw new RuntimeException(e);
        }

        try {
            specificStudent = studentsService.getStudentById(newStudentId);
        } catch (StudentNotFoundException e) {
            System.out.println("Student with id " + newStudentId + " couldn't be created.");

            throw new RuntimeException(e);
        }

        System.out.println("Student with id " + newStudentId + " is in group " + specificStudent.getGroup().getName());
        System.out.println("Student with id " + newStudentId + " is in year " + specificStudent.getYear());
        System.out.println("Student with id " + newStudentId + " is named " + specificStudent.getName());


        System.out.println("Reassigning student with id " + newStudentId + " to group A1");

        try {
            studentsService.reassignStudent(newStudentId, "A1");
            System.out.println("Reassigned student with id " + newStudentId + " to group A1");

        } catch (StudentGroupReassignException e) {
            System.out.println("Student with id " + newStudentId + " couldn't be reassigned.");
            throw new RuntimeException(e);
        }

        try {
            specificStudent = studentsService.getStudentById(newStudentId);
        } catch (StudentNotFoundException e) {
            System.out.println("Student with id " + newStudentId + " couldn't be created.");

            throw new RuntimeException(e);
        }

        System.out.println("Student with id " + newStudentId + " is in group " + specificStudent.getGroup().getName());
        System.out.println("Student with id " + newStudentId + " is in year " + specificStudent.getYear());
        System.out.println("Student with id " + newStudentId + " is named " + specificStudent.getName());


        System.out.println("Updating student with id " + newStudentId + " (year to become 2)");

        try {
            studentsService.updateStudent(newStudentId, "test" + "newtestupdatename" + UUID.randomUUID(), 2);
        } catch (StudentUpdateException e) {
            System.out.println("Student with id " + newStudentId + " couldn't be updated.");
            throw new RuntimeException(e);
        }

        try {
            specificStudent = studentsService.getStudentById(newStudentId);
        } catch (StudentNotFoundException e) {
            System.out.println("Student with id " + newStudentId + " couldn't be created.");

            throw new RuntimeException(e);
        }

        System.out.println("Student with id " + newStudentId + " is in group " + specificStudent.getGroup().getName());
        System.out.println("Student with id " + newStudentId + " is in year " + specificStudent.getYear());
        System.out.println("Student with id " + newStudentId + " is named " + specificStudent.getName());


        System.out.println("Deleting student with id " + newStudentId);

        try {
            studentsService.deleteStudent(newStudentId);
        } catch (StudentNotFoundException e) {
            System.out.println("Student with id " + newStudentId + " couldn't be found for performing delete student.");
            throw new RuntimeException(e);
        } catch (StudentDeletionFailed e) {
            System.out.println("Student with id " + newStudentId + " couldn't be deleted.");
            throw new RuntimeException(e);
        }

        try {
            specificStudent = studentsService.getStudentById(newStudentId);
        } catch (StudentNotFoundException e) {
            System.out.println("Student with id " + newStudentId + " couldn't be created.");

            throw new RuntimeException(e);
        }

        if (specificStudent == null) {
            System.out.println("Student with id " + newStudentId + " doesn't exist.");
        } else {
            System.out.println("Student with id " + newStudentId + "  exists.");
        }
    }

    private void showcaseSampleCodeForRepo() {
        var students = this.studentsRepository.readAll();

        if (students.size() <= 45) {
            StudentGroup studentGroup = new StudentGroup();

            // check group validator
            // studentGroup.setName("1A1");
            studentGroup.setName("A1");
            studentGroup.setYear(1);
            studentGroup.setType(StudentGroup.Type.BACHELOR);

            studentGroup.setInsertTime(new Date());

            Student newStudent = new Student();
            newStudent.setGroup(studentGroup);
            newStudent.setYear(1);
            newStudent.setName("test" + UUID.randomUUID());
            newStudent.setInsertTime(new Date());

            try {
                this.studentsRepository.save(newStudent);
            } catch (Exception e) {
                System.out.println("Couldn't insert student.");
                e.printStackTrace();
            }

            students = this.studentsRepository.readAll();
        }

        System.out.println("************************************** STUDENTS LIST *************************");

        for (var student : students) {
            System.out.println(student.getName());
        }

        System.out.println("************************************** END STUDENTS LIST *************************");
    }
}
