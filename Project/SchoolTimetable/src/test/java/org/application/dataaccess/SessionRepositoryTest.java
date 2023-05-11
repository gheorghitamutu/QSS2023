package org.application.dataaccess;

import org.application.Application;
import org.application.GuiceInjectorSingleton;
import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.database.TestsDatabaseHibernateProvider;
import org.application.dataaccess.discipline.DisciplineRepository;
import org.application.dataaccess.session.SessionRepository;
import org.application.databaseseed.TimetableEntitiesFactory;
import org.application.di.TestsDI;
import org.application.domain.exceptions.RepositoryOperationException;
import org.application.domain.exceptions.Timeslot.TimeslotDeletionFailed;
import org.application.domain.exceptions.discipline.DisciplineDeletionFailed;
import org.application.domain.exceptions.room.RoomDeletionFailed;
import org.application.domain.exceptions.session.SessionDeletionFailed;
import org.application.domain.exceptions.student.StudentDeletionFailed;
import org.application.domain.exceptions.studentgroup.StudentGroupDeletionFailed;
import org.application.domain.exceptions.teacher.TeacherDeletionFailed;
import org.application.domain.models.Discipline;
import org.application.domain.models.Session;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SessionRepositoryTest {

    private SessionRepository sessionRepository;
    private DisciplineRepository disciplineRepository;
    private Application app;


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @BeforeAll
    void setUpAll() {
        TestsDI.initializeDi();

        IHibernateProvider provider = new TestsDatabaseHibernateProvider();

        sessionRepository = new SessionRepository(provider);
        disciplineRepository = new DisciplineRepository(provider);

        this.app = GuiceInjectorSingleton.INSTANCE.getInjector().getInstance(Application.class);
        new TimetableEntitiesFactory(app).createTimetableEntities();
    }

    @AfterAll
    void tearDownAll() throws TimeslotDeletionFailed, TeacherDeletionFailed, StudentDeletionFailed, StudentGroupDeletionFailed, SessionDeletionFailed, RoomDeletionFailed, DisciplineDeletionFailed {
        app.disciplinesService.deleteAll();
        app.roomsService.deleteAll();
        app.sessionsService.deleteAll();
        app.studentGroupsService.deleteAll();
        app.studentsService.deleteAll();
        app.teachersService.deleteAll();
        app.timeslotsService.deleteAll();
    }

//    public Session createNewSession(Session.Type type, String halfYear) throws RepositoryOperationException

    @Test
    public void Given__SessionRepository__When__createNewSession__Then__CreateANewSessionForDisciplineAndReturnIt() throws RepositoryOperationException {

        var discipline = disciplineRepository.getByName("Discipline 01");

        Session session = new Session();
        session.setType(Session.Type.COURSE);
        session.setInsertTime(new Date());
        session.setDiscipline(discipline);

        sessionRepository.save(session);
    }





    @Test()
    public void readSession() {
        List<Session> sessions = sessionRepository.readAll();
        Assertions.assertTrue(sessions.size() > 1);
        for (Session r : sessions) {
            System.out.println(r);
        }
    }
}
