package org.application.models;

import org.application.DatabaseManager;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SessionTest {
    @BeforeAll
    public void setup() throws Exception {
    }

    @AfterAll
    void tearDownForAll() {
    }

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
        List<Session> sessions = DatabaseManager.read(Session.class);
        DatabaseManager.deleteMany(sessions);
    }

    @Test
    public void saveSession() {
        Discipline discipline = new Discipline();
        discipline.setCredits(6);
        discipline.setName("test");
        discipline.setInsertTime(new Date());
        Assertions.assertTrue(DatabaseManager.save(discipline));

        Session session = new Session();
        session.setType(0);
        session.setInsertTime(new Date());
        session.setDiscipline(discipline);
        Assertions.assertTrue(DatabaseManager.save(session));
    }

    @Test()
    public void readSession() {
        List<Session> sessions = DatabaseManager.read(Session.class);
        Assertions.assertEquals(1, sessions.size());
        for (Session r : sessions) {
            System.out.println(r);
        }
    }
}
