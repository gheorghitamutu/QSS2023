package org.application.models;

import org.application.DatabaseManager;
import org.junit.jupiter.api.*;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TimeslotTest {
    @BeforeAll
    public void setup() throws Exception {
    }

    @AfterAll
    void tearDownForAll() {
    }

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @BeforeAll
    void setUpAll() {
    }

    @AfterAll
    void tearDownAll() {
        List<Discipline> disciplines = DatabaseManager.readAll(Discipline.class);
        DatabaseManager.deleteMany(disciplines);

        List<Session> sessions = DatabaseManager.readAll(Session.class);
        DatabaseManager.deleteMany(sessions);

        List<Room> rooms = DatabaseManager.readAll(Room.class);
        DatabaseManager.deleteMany(rooms);

        List<Timeslot> timeslots = DatabaseManager.readAll(Timeslot.class);
        DatabaseManager.deleteMany(timeslots);
    }

    @Test
    public void saveTimeslot() {
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
        Assertions.assertTrue(DatabaseManager.save(discipline));

        Session session = new Session();
        session.setType(Session.Type.COURSE);
        session.setInsertTime(new Date());
        session.setDiscipline(discipline);
        Assertions.assertTrue(DatabaseManager.save(session));
        timeslot.setSession(session);

        room.setTimeslots(Collections.singleton(timeslot));
        Assertions.assertTrue(DatabaseManager.save(room));
        Assertions.assertTrue(DatabaseManager.save(timeslot));
    }

    @Test()
    public void readTimeslot() {
        List<Timeslot> timeslots = DatabaseManager.readAll(Timeslot.class);
        Assertions.assertEquals(1, timeslots.size());
        for (Timeslot t : timeslots) {
            System.out.println(t);
        }
    }
}
