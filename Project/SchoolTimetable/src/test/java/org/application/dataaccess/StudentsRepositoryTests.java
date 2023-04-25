package org.application.dataaccess;

import org.application.DatabaseManager;
import org.application.models.Student;
import org.application.models.StudentGroup;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StudentsRepositoryTests {


    private StudentsRepository studentsRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @BeforeAll
    void setUpAll() {
        this.studentsRepository = new StudentsRepository(new TestsDatabaseHibernateProvider());
    }

    @AfterAll
    void tearDownAll() {
        List<Student> students = studentsRepository.readAll(Student.class);
        studentsRepository.deleteMany(students);
    }

    @Test
    public void saveStudent() {
        StudentGroup studentGroup = new StudentGroup();
        studentGroup.setName("A1");
        studentGroup.setInsertTime(new Date());

        Student student = new Student();
        student.setGroup(studentGroup);
        student.setYear(1);
        student.setName("test");
        student.setInsertTime(new Date());
        Assertions.assertTrue(studentsRepository.save(student));
    }

    @Test()
    public void readStudent() {
        List<Student> students = studentsRepository.readAll(Student.class);
        Assertions.assertEquals(1, students.size());
        for (Student r : students) {
            System.out.println(r);
        }
    }
}
