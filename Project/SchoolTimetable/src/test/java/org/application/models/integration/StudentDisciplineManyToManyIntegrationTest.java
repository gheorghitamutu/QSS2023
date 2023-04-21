package org.application.models.integration;

import org.application.DatabaseManager;
import org.application.models.Discipline;
import org.application.models.Student;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentDisciplineManyToManyIntegrationTest {

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

        List<Discipline> disciplines = DatabaseManager.read(Discipline.class);
        DatabaseManager.deleteMany(disciplines);
    }

    @Test
    public void saveStudentDiscipline() {
        Discipline discipline = new Discipline();
        discipline.setCredits(6);
        discipline.setName("test");
        discipline.setInsertTime(new Date());
        Assertions.assertTrue(DatabaseManager.save(discipline));

        Student student = new Student();
        student.setGroup("A");
        student.setYear(1);
        student.setName("test");
        student.setInsertTime(new Date());
        Set<Discipline> disciplines = new HashSet<>();
        disciplines.add(discipline);
        student.setDisciplines(disciplines);

        Assertions.assertTrue(DatabaseManager.save(student));
    }
}
