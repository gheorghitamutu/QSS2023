package org.services;

import org.Application;
import org.GuiceInjectorSingleton;
import org.di.TestsDI;
import org.domain.exceptions.Timeslot.TimeslotAdditionException;
import org.domain.exceptions.Timeslot.TimeslotDeletionFailed;
import org.domain.exceptions.discipline.DisciplineAdditionException;
import org.domain.exceptions.discipline.DisciplineDeletionFailed;
import org.domain.exceptions.discipline.DisciplineNotFoundException;
import org.domain.exceptions.room.RoomAdditionException;
import org.domain.exceptions.room.RoomDeletionFailed;
import org.domain.exceptions.session.SessionAdditionException;
import org.domain.exceptions.session.SessionDeletionFailed;
import org.domain.exceptions.student.StudentAdditionException;
import org.domain.exceptions.student.StudentDeletionFailed;
import org.domain.exceptions.studentgroup.StudentGroupAdditionException;
import org.domain.exceptions.studentgroup.StudentGroupDeletionFailed;
import org.domain.exceptions.teacher.TeacherAdditionException;
import org.domain.exceptions.teacher.TeacherDeletionFailed;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.*;
import org.junit.jupiter.api.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Collections;
import java.util.HashSet;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TimeslotServiceTest {
    private Application app;

    @BeforeAll
    void setUpAll() {
        TestsDI.initializeDi();
        this.app = GuiceInjectorSingleton.INSTANCE.getInjector().getInstance(Application.class);
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
    public void Given_TimeslotService_When_doubleOverlappingAddition_Then_throwAdditionException() throws TeacherAdditionException, DisciplineAdditionException, StudentAdditionException, StudentGroupAdditionException, SessionAdditionException, DisciplineNotFoundException, RoomAdditionException, ParseException, TimeslotAdditionException, ValidationException {
        var teacher = app.teachersService.addTeacher("Teacher 001", Teacher.Type.TEACHER);
        var collaborator = app.teachersService.addTeacher("Teacher 002", Teacher.Type.COLLABORATOR);

        var discipline = app.disciplinesService.addDiscipline("Discipline 001", 6);
        discipline.setTeachers(new HashSet<>() {{
            add(teacher);
            add(collaborator);
        }});

        teacher.setDisciplines(Collections.singleton(discipline));
        collaborator.setDisciplines(Collections.singleton(discipline));

        var student = app.studentsService.addStudent("Student 001", "310910204006SM000005", 1, "B1");

        var group = app.studentGroupsService.addStudentGroup("B1", 1, StudentGroup.Type.BACHELOR);

        student.setGroup(group);
        student.setDisciplines(Collections.singleton(discipline));

        var course = app.sessionsService.addSession(Session.Type.COURSE, "B", "Discipline 001");
        var laboratory = app.sessionsService.addSession(Session.Type.LABORATORY, "B", "Discipline 001");

        course.setDiscipline(discipline);
        course.setGroups(Collections.singleton(group));
        laboratory.setDiscipline(discipline);
        laboratory.setGroups(Collections.singleton(group));

        teacher.setSessions(Collections.singleton(course));
        collaborator.setSessions(Collections.singleton(laboratory));

        var room = app.roomsService.addRoom("C200", 200, 1, Room.Type.COURSE);

        var timeslotCourse = app.timeslotsService.addTimeslot(new SimpleDateFormat("dd-MM-yyyy").parse("01-01-2023"), new SimpleDateFormat("dd-MM-yyyy").parse("01-07-2023"), new SimpleDateFormat("HH:mm:ss").parse("15:30:00"), Duration.ofMinutes(120), Timeslot.Day.MONDAY, Timeslot.Periodicity.WEEKLY, room, course);
        Assertions.assertNotNull(timeslotCourse);

        Assertions.assertThrowsExactly(TimeslotAdditionException.class, () -> {
            app.timeslotsService.addTimeslot(new SimpleDateFormat("dd-MM-yyyy").parse("01-01-2023"), new SimpleDateFormat("dd-MM-yyyy").parse("01-07-2023"), new SimpleDateFormat("HH:mm:ss").parse("15:30:00"), Duration.ofMinutes(120), Timeslot.Day.MONDAY, Timeslot.Periodicity.WEEKLY, room, laboratory);
        });
    }
}
