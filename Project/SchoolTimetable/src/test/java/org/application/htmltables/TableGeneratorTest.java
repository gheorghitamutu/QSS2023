package org.application.htmltables;

import com.google.inject.Inject;
import org.application.Application;
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
import org.application.domain.exceptions.Timeslot.TimeslotNotFoundException;
import org.application.domain.exceptions.discipline.DisciplineAdditionException;
import org.application.domain.exceptions.discipline.DisciplineDeletionFailed;
import org.application.domain.exceptions.discipline.DisciplineNotFoundException;
import org.application.domain.exceptions.room.RoomAdditionException;
import org.application.domain.exceptions.room.RoomDeletionFailed;
import org.application.domain.exceptions.room.RoomNotFoundException;
import org.application.domain.exceptions.session.SessionAdditionException;
import org.application.domain.exceptions.session.SessionDeletionFailed;
import org.application.domain.exceptions.session.SessionNotFoundException;
import org.application.domain.exceptions.student.StudentAdditionException;
import org.application.domain.exceptions.student.StudentDeletionFailed;
import org.application.domain.exceptions.student.StudentNotFoundException;
import org.application.domain.exceptions.student.StudentUpdateException;
import org.application.domain.exceptions.studentgroup.StudentGroupAdditionException;
import org.application.domain.exceptions.studentgroup.StudentGroupDeletionFailed;
import org.application.domain.exceptions.studentgroup.StudentGroupNotFoundException;
import org.application.domain.exceptions.teacher.TeacherAdditionException;
import org.application.domain.exceptions.teacher.TeacherDeletionFailed;
import org.application.domain.exceptions.teacher.TeacherNotFoundException;
import org.application.domain.models.*;
import org.application.presentation.GUI;
import org.application.presentation.MainGenerator;
import org.junit.jupiter.api.*;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TableGeneratorTest {

    private final String[] teacherNames = {
            "Teacher 01", "Teacher 02", "Teacher 03", "Teacher 04", "Teacher 05", "Teacher 06"
    };

    private final String[] studentNames = {
            "Student 01", "Student 02", "Student 03", "Student 04", "Student 05", "Student 06"
    };

    private final String[] studentRegistrationNumbers = {
            "310910204006SM000000", "310910204006SM000001", "310910204006SM000002",
            "310910204006SM000003", "310910204006SM000004", "310910204006SM000005"
    };

    private final String[] groupNames = {
            "A1", "A2", "A3", "A4", "A5", "A6"
    };

    private final String[] roomNames = {
            "C100", "C101", "C102"
    };

    private final String[] disciplineNames = {
            "Discipline 01", "Discipline 02", "Discipline 03", "Discipline 04"
    };


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
    public void TestSimpleUseCase() throws TeacherAdditionException, DisciplineAdditionException, StudentAdditionException, StudentGroupAdditionException, SessionAdditionException, java.text.ParseException, TimeslotAdditionException, RoomAdditionException, StudentUpdateException, StudentNotFoundException, DisciplineNotFoundException, TimeslotDeletionFailed, RoomNotFoundException, TimeslotNotFoundException, StudentGroupNotFoundException, SessionNotFoundException, TeacherNotFoundException {
        for (var name : this.teacherNames) {
            this.app.teachersService.addTeacher(name, Teacher.Type.TEACHER);
        }

        for (var name : this.groupNames) {
            this.app.studentGroupsService.addStudentGroup(name, 1, StudentGroup.Type.BACHELOR);
        }

        for (var i = 0; i < this.studentNames.length; i++) {
            this.app.studentsService.addStudent(
                    this.studentNames[i],
                    this.studentRegistrationNumbers[i],
                    1,
                    this.groupNames[i]
            );
        }

        for (var name : this.roomNames) {
            this.app.roomsService.addRoom(name, 30, 1, Room.Type.COURSE);
        }

        var teachers = this.app.teachersService.getTeachers();

        for (var name : this.disciplineNames) {
            this.app.disciplinesService.addDiscipline(name, 6);

            // TODO: add teachers to discipline

            this.app.disciplinesService.addTeacherToDiscipline(
                    teachers.get(0).getName(),
                    name
            );

            this.app.disciplinesService.addTeacherToDiscipline(
                    teachers.get(1).getName(),
                    name
            );


        }


        var disciplines = this.app.disciplinesService.getDisciplines();
        var rooms = this.app.roomsService.getRooms();

        for (int sessionIndex = 0; sessionIndex < Math.min(disciplines.size(), 4); sessionIndex++)
        {

            var session = this.app.sessionsService.addSession(
                    Session.Type.COURSE,
                    "A",
                    disciplines.get(sessionIndex).getName()
            );

            this.app.sessionsService.addGroupToSession(
                    session.getId(),
                    this.groupNames[0]
            );

            this.app.sessionsService.addGroupToSession(
                    session.getId(),
                    this.groupNames[1]
            );

            this.app.sessionsService.addGroupToSession(
                    session.getId(),
                    this.groupNames[2]
            );


            this.app.sessionsService.addTeacherToSession(
                    session.getId(),
                    this.teacherNames[0]
            );

            this.app.sessionsService.addTeacherToSession(
                    session.getId(),
                    this.teacherNames[1]
            );

            var startDate = new Date();

            // get date 6 months away from startdate
            var endDate = new Date(startDate.getTime() + 6L * 30 * 24 * 60 * 60 * 1000);

            var time = new SimpleDateFormat("HH:mm")
                    .parse(String.format("%02d:%02d", 8 + sessionIndex * 2, 0));

            var duration = Duration.ofHours(2);
            var day = Timeslot.Day.MONDAY;
            var periodicity = Timeslot.Periodicity.WEEKLY;
            var room = rooms.get(0);

            this.app.timeslotsService.addTimeslot(
                    startDate,
                    endDate,
                    time,
                    duration,
                    day,
                    periodicity,
                    room,
                    session
            );
        }

        disciplines = this.app.disciplinesService.getDisciplines();
        rooms = this.app.roomsService.getRooms();
        teachers = this.app.teachersService.getTeachers();
        var groups = this.app.studentGroupsService.getStudentGroups();
        var students = this.app.studentsService.getStudents();
        var timeslots = this.app.timeslotsService.getTimeslots();

//        GUI.setUpAll(true);

        GUI.app = this.app;

        MainGenerator generator = new MainGenerator(42);

        generator.generateLists();

        generator.generateTimetables();

        generator.saveAllData("./outtimetable");
    }
}
