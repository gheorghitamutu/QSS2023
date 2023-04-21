package org.application.models;

import org.application.DatabaseManager;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RoomTest {
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
        List<Room> rooms = DatabaseManager.read(Room.class);
        DatabaseManager.deleteMany(rooms);
    }

    @Test
    public void saveRoom() {
        Room room = new Room();
        room.setCapacity(30);
        room.setFloor(1);
        room.setName("test");
        room.setType(1);
        room.setInsertTime(new Date());
        Assertions.assertTrue(DatabaseManager.save(room));
    }

    @Test()
    public void readRoom() {
        List<Room> rooms = DatabaseManager.read(Room.class);
        Assertions.assertEquals(1, rooms.size());
        for (Room r : rooms) {
            System.out.println(r);
        }
    }
}
