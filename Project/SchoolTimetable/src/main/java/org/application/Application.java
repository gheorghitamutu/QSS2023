package org.application;

import com.google.inject.Inject;
import org.application.dataaccess.IStudentsRepository;
import org.application.models.Student;
import org.application.models.StudentGroup;

import java.util.Date;
import java.util.UUID;

public class Application {


    private final IStudentsRepository studentsRepository;

    @Inject
    public Application(IStudentsRepository studentsRepository) {

        this.studentsRepository = studentsRepository;
    }

    public void run() {
        var students = this.studentsRepository.readAll();

        if (students.size() <= 15)
        {
            StudentGroup studentGroup = new StudentGroup();


            // check group validator
//            studentGroup.setName("A1");
            studentGroup.setName("1A1sadas");

            studentGroup.setInsertTime(new Date());

            Student newStudent = new Student();
            newStudent.setGroup(studentGroup);
            newStudent.setYear(1);
            newStudent.setName("test" + UUID.randomUUID());
            newStudent.setInsertTime(new Date());

            var succeded = this.studentsRepository.save(newStudent);

            if (!succeded)
            {
                System.out.println("Couldn't insert student.");
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
