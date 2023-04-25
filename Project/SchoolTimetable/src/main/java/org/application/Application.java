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

        if (students.size() <= 5)
        {
            var newStudent = new Student();

            StudentGroup studentGroup = new StudentGroup();
            studentGroup.setName("A1");
            studentGroup.setInsertTime(new Date());

            Student student = new Student();
            student.setGroup(studentGroup);
            student.setYear(1);
            student.setName("test" + UUID.randomUUID());
            student.setInsertTime(new Date());

            this.studentsRepository.save(student);

            students = this.studentsRepository.readAll();
        }

        System.out.println("************************************** STUDENTS LIST *************************");

        for (var student :
                students) {
            System.out.println(student.getName());
        }

        System.out.println("************************************** END STUDENTS LIST *************************");

    }
}
