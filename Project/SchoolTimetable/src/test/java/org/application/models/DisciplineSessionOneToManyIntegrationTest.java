package org.application.models;

import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.database.TestsDatabaseHibernateProvider;
import org.application.dataaccess.discipline.DisciplineRepository;
import org.application.dataaccess.session.SessionRepository;
import org.application.domain.models.Discipline;
import org.application.domain.models.Session;
import org.junit.jupiter.api.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DisciplineSessionOneToManyIntegrationTest {

    private DisciplineRepository disciplineRepository;
    private SessionRepository sessionRepository;

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
    public void saveDisciplineSession() {
        Discipline discipline = new Discipline();
        discipline.setCredits(6);
        discipline.setName("test");
        discipline.setInsertTime(new Date());

        Session session = new Session();
        session.setInsertTime(new Date());
        session.setType(Session.Type.COURSE);
        session.setDiscipline(discipline);

        discipline.setSessions(Collections.singleton(session));

        Assertions.assertTrue(disciplineRepository.save(discipline));
        Assertions.assertTrue(sessionRepository.save(session));
    }
}
