package org.application.dataaccess;

import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.discipline.DisciplineRepository;
import org.application.dataaccess.database.TestsDatabaseHibernateProvider;
import org.application.dataaccess.session.SessionRepository;
import org.application.domain.models.Discipline;
import org.application.domain.models.Session;
import org.application.domain.models.Student;
import org.application.domain.models.StudentGroup;
import org.junit.jupiter.api.*;

import java.util.Collections;
import java.util.Date;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class DisciplineRepositoryTest {

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
        disciplineRepository = new DisciplineRepository(provider);
        sessionRepository = new SessionRepository(provider);
    }


    @AfterAll
    void tearDownAll() {
        List<Discipline> disciplines = disciplineRepository.readAll();
        disciplineRepository.deleteMany(disciplines);

        List<Session> sessions = sessionRepository.readAll();
        sessionRepository.deleteMany(sessions);
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

        Assertions.assertTrue(disciplineRepository.save(discipline));
    }

    @Test()
    public void readDiscipline() {
        List<Discipline> disciplines = disciplineRepository.readAll();
        Assertions.assertEquals(1, disciplines.size());
        for (Discipline d : disciplines) {
            System.out.println(d);
        }
    }
}
