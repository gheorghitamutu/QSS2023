package org.application;

import org.application.models.Room;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RoomTest {
    DatabaseManager dbManager;

    @BeforeAll
    public void setup() throws Exception {
        try {
            dbManager = new DatabaseManager();
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }
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

    @Test
    public void saveRoom() {
        Room room = new Room();
        room.setCapacity(30);
        room.setFloor(1);
        room.setName("test");
        room.setType(1);
        room.setInsertTime(new Date());
        Assertions.assertTrue(dbManager.saveRoom(room));
    }

    @Test()
    public void readRoom() {
        List<Room> rooms = dbManager.readRooms();
        Assertions.assertEquals(1, rooms.size());
        for (Room r : rooms) {
            System.out.println(r);
        }
    }
}