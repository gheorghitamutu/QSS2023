package org.models;

import org.Application;
import org.GuiceInjectorSingleton;
import org.dataaccess.database.IHibernateProvider;
import org.dataaccess.database.TestsDatabaseHibernateProvider;
import org.dataaccess.discipline.DisciplineRepository;
import org.dataaccess.session.SessionRepository;
import org.databaseseed.TimetableEntitiesFactory;
import org.di.TestsDI;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.Timeslot.TimeslotDeletionFailed;
import org.domain.exceptions.discipline.DisciplineDeletionFailed;
import org.domain.exceptions.room.RoomDeletionFailed;
import org.domain.exceptions.session.SessionDeletionFailed;
import org.domain.exceptions.student.StudentDeletionFailed;
import org.domain.exceptions.studentgroup.StudentGroupDeletionFailed;
import org.domain.exceptions.teacher.TeacherDeletionFailed;
import org.domain.models.Discipline;
import org.domain.models.Session;
import org.junit.jupiter.api.*;

import java.util.Collections;
import java.util.Date;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DisciplineSessionOneToManyIntegrationTest {

    private DisciplineRepository disciplineRepository;
    private SessionRepository sessionRepository;
    private Application app;

    @BeforeAll
    void setUpAll() {

        TestsDI.initializeDi();

        IHibernateProvider provider = new TestsDatabaseHibernateProvider();
        sessionRepository = new SessionRepository(provider);
        disciplineRepository = new DisciplineRepository(provider);

        this.app = GuiceInjectorSingleton.INSTANCE.getInjector().getInstance(Application.class);
    }

    @AfterAll
    void tearDownAll() throws RepositoryOperationException, TimeslotDeletionFailed, TeacherDeletionFailed, StudentDeletionFailed, StudentGroupDeletionFailed, SessionDeletionFailed, RoomDeletionFailed, DisciplineDeletionFailed {
        app.disciplinesService.deleteAll();
        app.roomsService.deleteAll();
        app.sessionsService.deleteAll();
        app.studentGroupsService.deleteAll();
        app.studentsService.deleteAll();
        app.teachersService.deleteAll();
        app.timeslotsService.deleteAll();
    }

    @Test
    public void saveDisciplineSession() throws RepositoryOperationException {
        Discipline discipline = new Discipline();
        discipline.setCredits(6);
        discipline.setName("test");
        discipline.setInsertTime(new Date());

        Session session = new Session();
        session.setInsertTime(new Date());
        session.setType(Session.Type.COURSE);
        session.setDiscipline(discipline);

        discipline.setSessions(Collections.singleton(session));

        disciplineRepository.save(discipline);
        sessionRepository.save(session);

        Assertions.assertEquals(1, disciplineRepository.readAll().size());
        Assertions.assertEquals(1, sessionRepository.readAll().size());
    }
}
