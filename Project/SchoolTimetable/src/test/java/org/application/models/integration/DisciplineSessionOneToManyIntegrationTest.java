package org.application.models.integration;

import org.application.DatabaseManager;
import org.application.models.Discipline;
import org.application.models.Session;
import org.application.models.Student;
import org.junit.jupiter.api.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DisciplineSessionOneToManyIntegrationTest {
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
        List<Discipline> disciplines = DatabaseManager.read(Discipline.class);
        DatabaseManager.deleteMany(disciplines);

        List<Session> sessions = DatabaseManager.read(Session.class);
        DatabaseManager.deleteMany(sessions);
    }

    @Test
    public void saveDisciplineSession() {
        Discipline discipline = new Discipline();
        discipline.setCredits(6);
        discipline.setName("test");
        discipline.setInsertTime(new Date());

        Session session = new Session();
        session.setInsertTime(new Date());
        session.setType(0);
        session.setDiscipline(discipline);

        discipline.setSessions(Collections.singleton(session));

        Assertions.assertTrue(DatabaseManager.save(discipline));
        Assertions.assertTrue(DatabaseManager.save(session));
    }
}
