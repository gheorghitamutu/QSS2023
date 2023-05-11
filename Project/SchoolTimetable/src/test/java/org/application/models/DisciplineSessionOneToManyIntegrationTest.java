package org.application.models;

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

import java.util.Collections;
import java.util.Date;
import java.util.List;

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
        new TimetableEntitiesFactory(app).createTimetableEntities();
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
    }
}
