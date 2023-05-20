package org.services;

import org.Application;
import org.GuiceInjectorSingleton;
import org.Main;
import org.di.TestsDI;
import org.domain.exceptions.Timeslot.TimeslotAdditionException;
import org.domain.exceptions.Timeslot.TimeslotDeletionFailed;
import org.domain.exceptions.Timeslot.TimeslotNotFoundException;
import org.domain.exceptions.discipline.DisciplineAdditionException;
import org.domain.exceptions.discipline.DisciplineDeletionFailed;
import org.domain.exceptions.discipline.DisciplineNotFoundException;
import org.domain.exceptions.room.RoomAdditionException;
import org.domain.exceptions.room.RoomDeletionFailed;
import org.domain.exceptions.room.RoomNotFoundException;
import org.domain.exceptions.session.SessionAdditionException;
import org.domain.exceptions.session.SessionDeletionFailed;
import org.domain.exceptions.student.StudentAdditionException;
import org.domain.exceptions.student.StudentDeletionFailed;
import org.domain.exceptions.student.StudentNotFoundException;
import org.domain.exceptions.student.StudentUpdateException;
import org.domain.exceptions.studentgroup.StudentGroupAdditionException;
import org.domain.exceptions.studentgroup.StudentGroupDeletionFailed;
import org.domain.exceptions.teacher.TeacherAdditionException;
import org.domain.exceptions.teacher.TeacherDeletionFailed;
import org.domain.models.*;
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

        var appInjector = Main.setupDependenciesInjector(true);
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
    public void TestSimpleUseCase() throws TeacherAdditionException, DisciplineAdditionException, StudentAdditionException, StudentGroupAdditionException, SessionAdditionException, java.text.ParseException, TimeslotAdditionException, RoomAdditionException, StudentUpdateException, StudentNotFoundException, DisciplineNotFoundException, TimeslotDeletionFailed, RoomNotFoundException, TimeslotNotFoundException {
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
        var laboratory = app.sessionsService.addSession(Session.Type.LABORATORY, "A", "Discipline 01");

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

        var tssSorted = app.timeslotsService.getSortedTimeslotsByStartDateAndTime();
        Assertions.assertEquals(tssSorted.size(), 2);
        Assertions.assertTrue(tssSorted.get(0).getStartDate().before(tssSorted.get(1).getStartDate()) || (tssSorted.get(0).getStartDate().equals(tssSorted.get(1).getStartDate()) && tssSorted.get(0).getTime().before(tssSorted.get(1).getTime())) || (tssSorted.get(0).getStartDate().equals(tssSorted.get(1).getStartDate()) && tssSorted.get(0).getTime().equals(tssSorted.get(1).getTime())));

        app.timeslotsService.deleteTimeslot(new SimpleDateFormat("dd-MM-yyyy").parse("01-01-2023"), new SimpleDateFormat("HH:mm:ss").parse("15:30:00"), Duration.ofMinutes(120), "C100");
    }
}
