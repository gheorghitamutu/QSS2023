package org.application.models.integration;

import org.application.DatabaseManager;
import org.application.models.Discipline;
import org.application.models.Group;
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
        List<Student> students = DatabaseManager.readAll(Student.class);
        DatabaseManager.deleteMany(students);

        List<Discipline> disciplines = DatabaseManager.readAll(Discipline.class);
        DatabaseManager.deleteMany(disciplines);
    }

    @Test
    public void saveStudentDiscipline() {
        Discipline discipline = new Discipline();
        discipline.setCredits(6);
        discipline.setName("test");
        discipline.setInsertTime(new Date());
        Assertions.assertTrue(DatabaseManager.save(discipline));

        Group group = new Group();
        group.setName("A1");
        group.setInsertTime(new Date());

        Student student = new Student();
        student.setGroup(group);
        student.setYear(1);
        student.setName("test");
        student.setInsertTime(new Date());
        Set<Discipline> disciplines = new HashSet<>();
        disciplines.add(discipline);
        student.setDisciplines(disciplines);

        Assertions.assertTrue(DatabaseManager.save(student));
    }
}
