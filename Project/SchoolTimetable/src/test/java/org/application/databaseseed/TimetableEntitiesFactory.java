package org.application.databaseseed;

import org.application.Application;

import org.application.GuiceInjectorSingleton;
import org.application.di.TestsDI;
import org.application.domain.models.*;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class TimetableEntitiesFactory {


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

    //make fields for the other entities

    Application app;


    public TimetableEntitiesFactory(Application app) {
        this.app = app;
    }
    public void createTimetableEntities() {
        try {
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

            for (int sessionIndex = 0; sessionIndex < Math.min(disciplines.size(), 4); sessionIndex++) {

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

            //save to static fields


        } catch (Exception e) {
            throw new RuntimeException("Seed failed!", e);
        }
    }
}
