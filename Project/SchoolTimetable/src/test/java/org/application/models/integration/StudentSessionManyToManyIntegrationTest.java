package org.application.models.integration;

import org.application.DatabaseManager;
import org.application.models.Discipline;
import org.application.models.StudentGroup;
import org.application.models.Session;
import org.application.models.Student;
import org.junit.jupiter.api.*;

import java.util.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentSessionManyToManyIntegrationTest {
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

        List<Session> sessions = DatabaseManager.readAll(Session.class);
        DatabaseManager.deleteMany(sessions);
    }

    @Test
    public void saveStudentDiscipline() {
        Discipline discipline = new Discipline();
        discipline.setCredits(6);
        discipline.setName("test");
        discipline.setInsertTime(new Date());

        Session session = new Session();
        session.setInsertTime(new Date());
        session.setType(Session.Type.SEMINARY);

        discipline.setSessions(Collections.singleton(session));
        Assertions.assertTrue(DatabaseManager.save(discipline));

        StudentGroup studentGroup = new StudentGroup();
        studentGroup.setName("A1");
        studentGroup.setInsertTime(new Date());

        Student student = new Student();
        student.setGroup(studentGroup);
        student.setYear(1);
        student.setName("test");
        student.setInsertTime(new Date());
        student.setDisciplines(Collections.singleton(discipline));

        session.setDiscipline(discipline);
        Assertions.assertTrue(DatabaseManager.save(session));

        Assertions.assertTrue(DatabaseManager.save(student));
    }
}
