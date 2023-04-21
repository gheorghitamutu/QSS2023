package org.application.models;

import org.application.DatabaseManager;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StudentTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @BeforeAll
    void setUpAll() {
    }

    @AfterAll
    void tearDownAll() {
        List<Student> students = DatabaseManager.read(Student.class);
        DatabaseManager.deleteMany(students);
    }

    @Test
    public void saveStudent() {
        Student student = new Student();
        student.setGroup("A");
        student.setYear(1);
        student.setName("test");
        student.setInsertTime(new Date());
        Assertions.assertTrue(DatabaseManager.save(student));
    }

    @Test()
    public void readStudent() {
        List<Student> students = DatabaseManager.read(Student.class);
        Assertions.assertEquals(1, students.size());
        for (Student r : students) {
            System.out.println(r);
        }
    }
}
