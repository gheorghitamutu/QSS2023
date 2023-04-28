package org.application.dataaccess;

import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.database.TestsDatabaseHibernateProvider;
import org.application.dataaccess.discipline.DisciplineRepository;
import org.application.dataaccess.session.SessionRepository;
import org.application.models.Discipline;
import org.application.models.Session;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SessionRepositoryTest {

    private SessionRepository sessionRepository;
    private DisciplineRepository disciplineRepository;

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
        IHibernateProvider provider = new TestsDatabaseHibernateProvider();
        sessionRepository = new SessionRepository(provider);
        disciplineRepository = new DisciplineRepository(provider);
    }

    @AfterAll
    void tearDownAll() {
        List<Discipline> disciplines = disciplineRepository.readAll();
        disciplineRepository.deleteMany(disciplines);

        List<Session> sessions = sessionRepository.readAll();
        sessionRepository.deleteMany(sessions);
    }

    @Test
    public void saveSession() {
        Discipline discipline = new Discipline();
        discipline.setCredits(6);
        discipline.setName("test");
        discipline.setInsertTime(new Date());

        Session session = new Session();
        session.setType(Session.Type.COURSE);
        session.setInsertTime(new Date());
        session.setDiscipline(discipline);
        Assertions.assertTrue(sessionRepository.save(session));
    }

    @Test()
    public void readSession() {
        List<Session> sessions = sessionRepository.readAll();
        Assertions.assertEquals(1, sessions.size());
        for (Session r : sessions) {
            System.out.println(r);
        }
    }
}
