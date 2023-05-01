package org.application.dataaccess;

import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.database.TestsDatabaseHibernateProvider;
import org.application.dataaccess.discipline.DisciplineRepository;
import org.application.dataaccess.room.RoomRepository;
import org.application.dataaccess.session.SessionRepository;
import org.application.dataaccess.timeslot.TimeslotRepository;
import org.application.di.TestsDI;
import org.application.domain.exceptions.RepositoryOperationException;
import org.application.domain.models.Discipline;
import org.application.domain.models.Room;
import org.application.domain.models.Session;
import org.application.domain.models.Timeslot;
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

    @BeforeAll
    public void setup() throws Exception {
        IHibernateProvider provider = new TestsDatabaseHibernateProvider();
        sessionRepository = new SessionRepository(provider);
        disciplineRepository = new DisciplineRepository(provider);
        roomRepository = new RoomRepository(provider);
        timeslotRepository = new TimeslotRepository(provider);
    }

    @AfterAll
    void tearDownForAll() {
    }

    @BeforeEach
    void setUp() {
        TestsDI.initializeDi();
    }

    @AfterEach
    void tearDown() {
    }

    @BeforeAll
    void setUpAll() {
    }

    @AfterAll
    void tearDownAll() throws RepositoryOperationException {
        List<Discipline> disciplines = disciplineRepository.readAll();
        disciplineRepository.deleteMany(disciplines);

        List<Session> sessions = sessionRepository.readAll();
        sessionRepository.deleteMany(sessions);

        List<Room> rooms = roomRepository.readAll();
        roomRepository.deleteMany(rooms);

        List<Timeslot> timeslots = timeslotRepository.readAll();
        timeslotRepository.deleteMany(timeslots);
    }

    @Test
    public void saveTimeslot() throws RepositoryOperationException {
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
        try {
            timeslot.setTime(new SimpleDateFormat("HH:mm:ss").parse("15:30:14"));
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
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

        sessionRepository.save(session);

        timeslot.setSession(session);

        room.setTimeslots(Collections.singleton(timeslot));

        roomRepository.save(room);
        timeslotRepository.save(timeslot);
    }

    @Test()
    public void readTimeslot() {
        List<Timeslot> timeslots = timeslotRepository.readAll();
        Assertions.assertEquals(1, timeslots.size());
        for (Timeslot t : timeslots) {
            System.out.println(t);
        }
    }
}
