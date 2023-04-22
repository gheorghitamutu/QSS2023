package org.application.models.integration;

import org.application.DatabaseManager;
import org.application.models.Discipline;
import org.application.models.Session;
import org.application.models.Student;
import org.junit.jupiter.api.*;

import java.util.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentSessionManyToManyIntegration {
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

        List<Session> sessions = DatabaseManager.read(Session.class);
        DatabaseManager.deleteMany(sessions);
    }

    @Test
    public void saveStudentDiscipline() {
        Discipline discipline = new Discipline();
        discipline.setCredits(6);
        discipline.setName("test");
        discipline.setInsertTime(new Date());

        Session session = new Session();
        session.setType(0);

        discipline.setSessions(Collections.singleton(session));
        Assertions.assertTrue(DatabaseManager.save(discipline));

        Student student = new Student();
        student.setGroup("A");
        student.setYear(1);
        student.setName("test");
        student.setInsertTime(new Date());
        student.setDisciplines(Collections.singleton(discipline));
        // student.setSessions(Collections.singleton(session));

        session.setStudents(Collections.singleton(student));
        session.setDiscipline(discipline);
        Assertions.assertTrue(DatabaseManager.save(session));

        Assertions.assertTrue(DatabaseManager.save(student));
    }
}
