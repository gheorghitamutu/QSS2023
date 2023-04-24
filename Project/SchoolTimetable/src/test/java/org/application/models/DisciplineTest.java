package org.application.models;

import org.application.DatabaseManager;
import org.junit.jupiter.api.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DisciplineTest {

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
        List<Discipline> disciplines = DatabaseManager.readAll(Discipline.class);
        DatabaseManager.deleteMany(disciplines);
    }

    @Test
    public void saveDiscipline() {

        Session session = new Session();
        session.setType(Session.Type.COURSE);
        session.setInsertTime(new Date());

        Discipline discipline = new Discipline();
        discipline.setCredits(6);
        discipline.setName("test");
        discipline.setSessions(Collections.singleton(session));
        discipline.setInsertTime(new Date());

        session.setDiscipline(discipline);

        StudentGroup studentGroup = new StudentGroup();
        studentGroup.setName("A1");
        studentGroup.setInsertTime(new Date());

        Student student = new Student();
        student.setGroup(studentGroup);
        student.setYear(1);
        student.setName("test");
        student.setInsertTime(new Date());

        studentGroup.setSessions(Collections.singleton(session));
        session.setGroups(Collections.singleton(studentGroup));

        Assertions.assertTrue(DatabaseManager.save(discipline));
    }

    @Test()
    public void readDiscipline() {
        List<Discipline> disciplines = DatabaseManager.readAll(Discipline.class);
        Assertions.assertEquals(1, disciplines.size());
        for (Discipline d : disciplines) {
            System.out.println(d);
        }
    }
}