package org.application.services;

import com.google.inject.Inject;
import org.application.GuiceInjectorSingleton;
import org.application.Main;
import org.application.application.disciplines.IDisciplinesService;
import org.application.application.rooms.IRoomsService;
import org.application.application.sessions.ISessionsService;
import org.application.application.studentgroups.IStudentGroupsService;
import org.application.application.students.IStudentsService;
import org.application.application.teachers.ITeachersService;
import org.application.application.timeslots.ITimeslotsService;
import org.application.di.TestsDI;
import org.application.domain.exceptions.Timeslot.TimeslotAdditionException;
import org.application.domain.exceptions.Timeslot.TimeslotDeletionFailed;
import org.application.domain.exceptions.discipline.DisciplineAdditionException;
import org.application.domain.exceptions.discipline.DisciplineDeletionFailed;
import org.application.domain.exceptions.discipline.DisciplineNotFoundException;
import org.application.domain.exceptions.room.RoomAdditionException;
import org.application.domain.exceptions.room.RoomDeletionFailed;
import org.application.domain.exceptions.session.SessionAdditionException;
import org.application.domain.exceptions.session.SessionDeletionFailed;
import org.application.domain.exceptions.student.StudentAdditionException;
import org.application.domain.exceptions.student.StudentDeletionFailed;
import org.application.domain.exceptions.student.StudentNotFoundException;
import org.application.domain.exceptions.student.StudentUpdateException;
import org.application.domain.exceptions.studentgroup.StudentGroupAdditionException;
import org.application.domain.exceptions.studentgroup.StudentGroupDeletionFailed;
import org.application.domain.exceptions.teacher.TeacherAdditionException;
import org.application.domain.exceptions.teacher.TeacherDeletionFailed;
import org.application.domain.models.*;
import org.junit.jupiter.api.*;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Collections;
import java.util.HashSet;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MinimalAllServicesTest {

    Application app;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @BeforeAll
    void setUpAll() {
        TestsDI.initializeDi();

        var appInjector = Main.setupDependenciesInjector(false);
        app = appInjector.getInstance(Application.class);
        GuiceInjectorSingleton.INSTANCE.setInjector(appInjector);
    }

    @AfterAll
    void tearDownAll() throws DisciplineDeletionFailed, RoomDeletionFailed, SessionDeletionFailed, StudentGroupDeletionFailed, StudentDeletionFailed, TeacherDeletionFailed, TimeslotDeletionFailed {
        app.disciplinesService.deleteAll();
        app.roomsService.deleteAll();
        app.sessionsService.deleteAll();
        app.studentGroupsService.deleteAll();
        app.studentsService.deleteAll();
        app.teachersService.deleteAll();
        app.timeslotsService.deleteAll();
    }

    @Test
    public void TestSimpleUseCase() throws TeacherAdditionException, DisciplineAdditionException, StudentAdditionException, StudentGroupAdditionException, SessionAdditionException, java.text.ParseException, TimeslotAdditionException, RoomAdditionException, StudentUpdateException, StudentNotFoundException, DisciplineNotFoundException {
        var teacher = app.teachersService.addTeacher("Teacher 01", Teacher.Type.TEACHER);
        var collaborator = app.teachersService.addTeacher("Teacher 02", Teacher.Type.COLLABORATOR);

        var discipline = app.disciplinesService.addDiscipline("Discipline 01", 6);
        discipline.setTeachers(new HashSet<>() {{
            add(teacher);
            add(collaborator);
        }});

        teacher.setDisciplines(Collections.singleton(discipline));
        collaborator.setDisciplines(Collections.singleton(discipline));

        var student = app.studentsService.addStudent("Student 01", "310910204006SM000000", 1, "A1");

        var group = app.studentGroupsService.addStudentGroup("A1", 1, StudentGroup.Type.BACHELOR);

        student.setGroup(group);
        student.setDisciplines(Collections.singleton(discipline));

        var course = app.sessionsService.addSession(Session.Type.COURSE, "A", "Discipline 01");
        var laboratory = app.sessionsService.addSession(Session.Type.LABORATORY, null, "Discipline 01");

        course.setDiscipline(discipline);
        course.setGroups(Collections.singleton(group));
        laboratory.setDiscipline(discipline);
        laboratory.setGroups(Collections.singleton(group));

        teacher.setSessions(Collections.singleton(course));
        collaborator.setSessions(Collections.singleton(laboratory));

        var room = app.roomsService.addRoom("C100", 200, 1, Room.Type.COURSE);

        var timeslotCourse = app.timeslotsService.addTimeslot(new SimpleDateFormat("dd-MM-yyyy").parse("01-01-2023"), new SimpleDateFormat("dd-MM-yyyy").parse("01-07-2023"), new SimpleDateFormat("HH:mm:ss").parse("15:30:00"), Duration.ofMinutes(120), Timeslot.Day.MONDAY, Timeslot.Periodicity.WEEKLY, room, course);
        Assertions.assertNotNull(timeslotCourse);

        var timeslotLaboratory = app.timeslotsService.addTimeslot(new SimpleDateFormat("dd-MM-yyyy").parse("01-01-2023"), new SimpleDateFormat("dd-MM-yyyy").parse("01-07-2023"), new SimpleDateFormat("HH:mm:ss").parse("15:30:00"), Duration.ofMinutes(120), Timeslot.Day.TUESDAY, Timeslot.Periodicity.WEEKLY, room, laboratory);
        Assertions.assertNotNull(timeslotLaboratory);
    }

    public static class Application {
        private final IDisciplinesService disciplinesService;
        private final IRoomsService roomsService;
        private final ISessionsService sessionsService;
        private final IStudentGroupsService studentGroupsService;
        private final IStudentsService studentsService;
        private final ITeachersService teachersService;
        private final ITimeslotsService timeslotsService;

        @Inject
        public Application(IDisciplinesService disciplinesService, IRoomsService roomsService, ISessionsService sessionsService, IStudentGroupsService studentGroupsService, IStudentsService studentsService, ITeachersService teachersService, ITimeslotsService timeslotsService) {
            this.disciplinesService = disciplinesService;
            this.roomsService = roomsService;
            this.sessionsService = sessionsService;
            this.studentGroupsService = studentGroupsService;
            this.studentsService = studentsService;
            this.teachersService = teachersService;
            this.timeslotsService = timeslotsService;
        }
    }
}
