package org.application.models;

import org.application.DatabaseManager;
import org.junit.jupiter.api.*;

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
        List<Timeslot> timeslots = DatabaseManager.read(Timeslot.class);
        DatabaseManager.deleteMany(timeslots);
    }

    @Test
    public void saveTimeslot() {
        Timeslot timeslot = new Timeslot();
        timeslot.setPeriodicity("weekly");
        timeslot.setDayOfWeek("M");
        timeslot.setTimespan(30);
        timeslot.setTime(15);
        timeslot.setInsertTime(new Date());
        Assertions.assertTrue(DatabaseManager.save(timeslot));
    }

    @Test()
    public void readTimeslot() {
        List<Timeslot> timeslots = DatabaseManager.read(Timeslot.class);
        Assertions.assertEquals(1, timeslots.size());
        for (Timeslot t : timeslots) {
            System.out.println(t);
        }
    }
}
