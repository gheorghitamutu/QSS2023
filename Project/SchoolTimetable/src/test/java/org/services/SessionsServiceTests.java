package org.services;

import org.Application;
import org.GuiceInjectorSingleton;
import org.application.sessions.ISessionsService;
import org.application.sessions.SessionsService;
import org.dataaccess.discipline.DisciplineRepository;
import org.dataaccess.session.SessionRepository;
import org.dataaccess.studentgroup.StudentGroupRepository;
import org.dataaccess.teacher.TeacherRepository;
import org.databaseseed.TimetableEntitiesFactory;
import org.di.TestsDI;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.Timeslot.TimeslotDeletionFailed;
import org.domain.exceptions.discipline.DisciplineDeletionFailed;
import org.domain.exceptions.room.RoomDeletionFailed;
import org.domain.exceptions.session.SessionAdditionException;
import org.domain.exceptions.session.SessionDeletionFailed;
import org.domain.exceptions.session.SessionNotFoundException;
import org.domain.exceptions.student.StudentDeletionFailed;
import org.domain.exceptions.studentgroup.StudentGroupDeletionFailed;
import org.domain.exceptions.studentgroup.StudentGroupNotFoundException;
import org.domain.exceptions.teacher.TeacherDeletionFailed;
import org.domain.exceptions.teacher.TeacherNotFoundException;
import org.domain.models.Discipline;
import org.domain.models.Session;
import org.domain.models.StudentGroup;
import org.domain.models.Teacher;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class SessionsServiceTests {
    private Application app;

    @BeforeAll
    void setUpAll() {
        TestsDI.initializeDi();
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

    @Test
    public void Given__SessionsService__When__getSessionsWithAtLeastOneExistentSession__Then__ReturnTheFoundSession() {

        var sessionRepository = Mockito.mock(SessionRepository.class);
        var disciplineRepository = Mockito.mock(DisciplineRepository.class);
        var teacherRepository = Mockito.mock(TeacherRepository.class);
        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);

        var session1 = new Session(Session.Type.COURSE, "A");
        var session2 = new Session(Session.Type.COURSE, "B");

        Mockito.when(sessionRepository.readAll()).thenReturn(new ArrayList<>() {
            {
                add(session1);
            }

            {
                add(session2);
            }
        });

        ISessionsService sessionsService = new SessionsService(sessionRepository, disciplineRepository, teacherRepository, studentGroupRepository);

        Assertions.assertEquals(2, sessionsService.getSessions().size());
    }

    @Test
    public void Given__SessionsService__When__deleteSessionWithInexistentId__Then__ShouldThrow() {

        var sessionRepository = Mockito.mock(SessionRepository.class);
        var disciplineRepository = Mockito.mock(DisciplineRepository.class);
        var teacherRepository = Mockito.mock(TeacherRepository.class);
        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);

        Mockito.when(sessionRepository.getById(1)).thenReturn(null);

        ISessionsService sessionsService = new SessionsService(sessionRepository, disciplineRepository, teacherRepository, studentGroupRepository);

        Assertions.assertThrows(SessionNotFoundException.class, () -> sessionsService.deleteSession(1));
    }

    @Test
    public void Given__SessionsService__When__deleteAllIsCalledAndRepositoryFails__Then__ShouldWrapExceptionAndThrowItFurther() throws RepositoryOperationException {

        var sessionRepository = Mockito.mock(SessionRepository.class);
        var disciplineRepository = Mockito.mock(DisciplineRepository.class);
        var teacherRepository = Mockito.mock(TeacherRepository.class);
        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);

        var session1 = new Session(Session.Type.COURSE, "A");
        var session2 = new Session(Session.Type.COURSE, "B");

        Mockito.when(sessionRepository.readAll()).thenReturn(new ArrayList<>() {
            {
                add(session1);
            }

            {
                add(session2);
            }
        });

        Mockito.doThrow(new RepositoryOperationException("Repo failure")).when(sessionRepository).deleteMany(anyList());

        ISessionsService sessionsService = new SessionsService(sessionRepository, disciplineRepository, teacherRepository, studentGroupRepository);

        Assertions.assertThrows(SessionDeletionFailed.class, sessionsService::deleteAll);
    }

    @Test
    public void Given__SessionsService__When__deleteSessionIsCalledAndRepositoryFails__Then__ShouldWrapExceptionAndThrowItFurther() throws RepositoryOperationException {

        var sessionRepository = Mockito.mock(SessionRepository.class);
        var disciplineRepository = Mockito.mock(DisciplineRepository.class);
        var teacherRepository = Mockito.mock(TeacherRepository.class);
        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);

        var session1 = new Session(Session.Type.COURSE, "A");
        var discipline1 = new Discipline("discipline1", 4);
        discipline1.setSessions(new HashSet<>(List.of(session1)));

        Mockito.when(disciplineRepository.readAll()).thenReturn(new ArrayList<>() {
            {
                add(discipline1);
            }
        });

        Mockito.doThrow(new RepositoryOperationException("Repo failure")).when(sessionRepository).delete(any(Session.class));

        ISessionsService sessionsService = new SessionsService(sessionRepository, disciplineRepository, teacherRepository, studentGroupRepository);

        Assertions.assertThrows(SessionDeletionFailed.class, () -> sessionsService.deleteSession("discipline1"));
    }

    @Test
    public void Given__SessionsService__When__getSessionByIdIsCalledAndSessionsIsNotFound__Then__ShouldThrow() {

        var sessionRepository = Mockito.mock(SessionRepository.class);
        var disciplineRepository = Mockito.mock(DisciplineRepository.class);
        var teacherRepository = Mockito.mock(TeacherRepository.class);
        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);

        var session1 = new Session(Session.Type.COURSE, "A");

        session1.setId(1);

        Mockito.when(sessionRepository.getById(1)).thenReturn(null);

        ISessionsService sessionsService = new SessionsService(sessionRepository, disciplineRepository, teacherRepository, studentGroupRepository);

        Assertions.assertThrows(SessionNotFoundException.class, () -> sessionsService.getSessionById(1));
    }

    @Test
    public void Given__SessionsService__When__getSessionsByHalfYearIsCalledAndSessionIsFound__Then__ShouldReturnTheFoundSession() {

        var sessionRepository = Mockito.mock(SessionRepository.class);
        var disciplineRepository = Mockito.mock(DisciplineRepository.class);
        var teacherRepository = Mockito.mock(TeacherRepository.class);
        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);

        var session1 = new Session(Session.Type.COURSE, "A");
        var session2 = new Session(Session.Type.COURSE, "B");

        Mockito.when(sessionRepository.readAll()).thenReturn(new ArrayList<>() {
            {
                add(session1);
            }

            {
                add(session2);
            }
        });

        ISessionsService sessionsService = new SessionsService(sessionRepository, disciplineRepository, teacherRepository, studentGroupRepository);
        Assertions.assertEquals(List.of(session1), sessionsService.getSessionsByHalfYear("A"));
    }

    @Test
    public void Given__SessionsService__When__addSessionIsCalledAndRepositorySaveThrows__Then__ShouldWrapExceptionAndThrowAsWell() throws RepositoryOperationException {

        var sessionRepository = Mockito.mock(SessionRepository.class);
        var disciplineRepository = Mockito.mock(DisciplineRepository.class);
        var teacherRepository = Mockito.mock(TeacherRepository.class);
        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);

        var discipline1 = new Discipline("discipline1", 4);

        Mockito.doThrow(new RepositoryOperationException("")).when(sessionRepository).save(Mockito.any(Session.class));

        Mockito.when(disciplineRepository.readAll()).thenReturn(new ArrayList<>() {
            {
                add(discipline1);
            }
        });

        Mockito.when(sessionRepository.createNewSession(Session.Type.COURSE, "A")).thenReturn(new Session(Session.Type.COURSE, "A"));

        Assertions.assertThrows(SessionAdditionException.class, () -> {
            ISessionsService sessionsService = new SessionsService(sessionRepository, disciplineRepository, teacherRepository, studentGroupRepository);

            var session = sessionsService.addSession(Session.Type.COURSE, "A", discipline1.getName());
        });
    }

    @Test
    public void Given__SessionsService__When__addTeacherToSessionIsCalledAndTeacherIsNotFound__Then__ShouldThrow() {
        var sessionRepository = Mockito.mock(SessionRepository.class);
        var disciplineRepository = Mockito.mock(DisciplineRepository.class);
        var teacherRepository = Mockito.mock(TeacherRepository.class);
        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);

        var discipline1 = new Discipline("discipline1", 4);

        var teacher1 = new Teacher("teacher1", Teacher.Type.TEACHER);
        var teacher2 = new Teacher("teacher2", Teacher.Type.TEACHER);

        Mockito.when(disciplineRepository.readAll()).thenReturn(new ArrayList<>() {
            {
                add(discipline1);
            }
        });

        Mockito.when(teacherRepository.readAll()).thenReturn(new ArrayList<>() {
            {
                add(teacher1);
            }

            {
                add(teacher2);
            }
        });

        ISessionsService sessionsService = new SessionsService(sessionRepository, disciplineRepository, teacherRepository, studentGroupRepository);

        Assertions.assertThrows(TeacherNotFoundException.class, () -> sessionsService.addTeacherToSession("discipline1", "teacher3"));
    }

    @Test
    public void Given__SessionsService__When__addGroupToSessionIsCalledAndGroupIsNotFound__Then__ShouldThrow() {
        var sessionRepository = Mockito.mock(SessionRepository.class);
        var disciplineRepository = Mockito.mock(DisciplineRepository.class);
        var teacherRepository = Mockito.mock(TeacherRepository.class);
        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);

        var group1 = new StudentGroup("group1", 1, StudentGroup.Type.BACHELOR);
        var group2 = new StudentGroup("group2", 1, StudentGroup.Type.BACHELOR);

        Mockito.when(studentGroupRepository.readAll()).thenReturn(new ArrayList<>() {
            {
                add(group1);
            }

            {
                add(group2);
            }
        });

        ISessionsService sessionsService = new SessionsService(sessionRepository, disciplineRepository, teacherRepository, studentGroupRepository);

        Assertions.assertThrows(StudentGroupNotFoundException.class, () -> sessionsService.addGroupToSession("discipline1", "group3"));
    }
}
