package org.dataaccess;

import org.Application;
import org.GuiceInjectorSingleton;
import org.dataaccess.database.IHibernateProvider;
import org.dataaccess.database.TestsDatabaseHibernateProvider;
import org.dataaccess.discipline.DisciplineRepository;
import org.dataaccess.room.RoomRepository;
import org.dataaccess.session.SessionRepository;
import org.dataaccess.timeslot.TimeslotRepository;
import org.databaseseed.TimetableEntitiesFactory;
import org.di.TestsDI;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.Timeslot.TimeslotAdditionException;
import org.domain.exceptions.Timeslot.TimeslotDeletionFailed;
import org.domain.exceptions.discipline.DisciplineDeletionFailed;
import org.domain.exceptions.room.RoomDeletionFailed;
import org.domain.exceptions.session.SessionDeletionFailed;
import org.domain.exceptions.student.StudentDeletionFailed;
import org.domain.exceptions.studentgroup.StudentGroupDeletionFailed;
import org.domain.exceptions.teacher.TeacherDeletionFailed;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Discipline;
import org.domain.models.Room;
import org.domain.models.Session;
import org.domain.models.Timeslot;
import org.junit.jupiter.api.*;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TimeslotRepositoryTest {

    private DisciplineRepository disciplineRepository;
    private SessionRepository sessionRepository;
    private RoomRepository roomRepository;
    private TimeslotRepository timeslotRepository;
    private Application app;



    @BeforeAll
    void setUpAll() {

        TestsDI.initializeDi();

        IHibernateProvider provider = new TestsDatabaseHibernateProvider();

        sessionRepository = new SessionRepository(provider);
        disciplineRepository = new DisciplineRepository(provider);
        roomRepository = new RoomRepository(provider);
        timeslotRepository = new TimeslotRepository(provider);

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

//    public Timeslot createNewTimeslot(Date startDate, Date endDate, Date time, Duration duration, Timeslot.Day day, Timeslot.Periodicity periodicity, Room room, Session session) throws RepositoryOperationException
    @Test
    public void Given__TimeslotRepository__When__createTimeslotIsCalledWithValidDateAndTimes__Then__ShouldCreateTheTimeslotAndReturnIt() throws RepositoryOperationException, java.text.ParseException, TimeslotAdditionException, ValidationException {

        Room room = roomRepository.getByName("C100");
        Session session = sessionRepository.readAll().get(0);

        var startDate = new Date();

        // get date 6 months away from startdate
        var endDate = new Date(startDate.getTime() + 6L * 30 * 24 * 60 * 60 * 1000);

        var time = new SimpleDateFormat("HH:mm")
                .parse(String.format("%02d:%02d", 8, 0));

        var duration = Duration.ofHours(2);
        var day = Timeslot.Day.MONDAY;
        var periodicity = Timeslot.Periodicity.WEEKLY;

        this.timeslotRepository.createNewTimeslot(startDate, endDate, time, duration, day, periodicity, room, session);
    }

    @Test
    public void Given__TimeslotRepository__When__createTimeslotIsCalledWithInvalidDateAndTimes__Then__ShouldThrow() throws RepositoryOperationException, java.text.ParseException, TimeslotAdditionException, ValidationException {

        Room room = roomRepository.getByName("C100");
        Session session = sessionRepository.readAll().get(0);

        var startDate = new Date();

        // get date 6 months away from startdate
        var endDate = new Date(startDate.getTime() + 6L * 30 * 24 * 60 * 60 * 1000);

        var time = new SimpleDateFormat("HH:mm")
                .parse(String.format("%02d:%02d", 5, 0));

        var duration = Duration.ofHours(2);
        var day = Timeslot.Day.MONDAY;
        var periodicity = Timeslot.Periodicity.WEEKLY;

        Assertions.assertThrows(RepositoryOperationException.class, () -> {
            this.timeslotRepository.createNewTimeslot(startDate, endDate, time, duration, day, periodicity, room, session);
        });

    }


    @Test
    public void saveTimeslot() throws RepositoryOperationException, java.text.ParseException {
        Room room = new Room();
        room.setCapacity(30);
        room.setFloor(1);
        room.setName("test");
        room.setType(Room.Type.COURSE);
        room.setInsertTime(new Date());

        Timeslot timeslot = new Timeslot();
        timeslot.setPeriodicity(Timeslot.Periodicity.WEEKLY);
        timeslot.setWeekday(Timeslot.Day.MONDAY);
        timeslot.setTimespan(Duration.ofMinutes(30));
        timeslot.setStartDate(new SimpleDateFormat("dd-MM-yyyy").parse("01-01-2023"));
        timeslot.setEndDate(new SimpleDateFormat("dd-MM-yyyy").parse("01-07-2023"));
        timeslot.setTime(new SimpleDateFormat("HH:mm:ss").parse("15:30:14"));
        timeslot.setInsertTime(new Date());
        timeslot.setRoom(room);

        Discipline discipline = new Discipline();
        discipline.setCredits(6);
        discipline.setName("test");
        discipline.setInsertTime(new Date());

        disciplineRepository.save(discipline);

        Session session = new Session();
        session.setType(Session.Type.COURSE);
        session.setInsertTime(new Date());
        session.setDiscipline(discipline);
        session.setHalfYear("A");

        sessionRepository.save(session);

        timeslot.setSession(session);

        room.setTimeslots(Collections.singleton(timeslot));

        roomRepository.save(room);
        timeslotRepository.save(timeslot);
    }

    @Test()
    public void readTimeslot() {
        List<Timeslot> timeslots = timeslotRepository.readAll();
        Assertions.assertTrue(timeslots.size() > 1);
    }
}
