package org.application.dataaccess;

import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.database.TestsDatabaseHibernateProvider;
import org.application.dataaccess.room.RoomRepository;
import org.application.models.Room;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RoomRepositoryTest {

    private RoomRepository roomRepository;

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
        IHibernateProvider provider = new TestsDatabaseHibernateProvider();
        roomRepository = new RoomRepository(provider);
    }


    @AfterAll
    void tearDownAll() {
        List<Room> rooms = roomRepository.readAll();
        roomRepository.deleteMany(rooms);
    }

    @Test
    public void saveRoom() {
        Room room = new Room();
        room.setCapacity(30);
        room.setFloor(1);
        room.setName("test");
        room.setType(Room.Type.COURSE);
        room.setInsertTime(new Date());
        Assertions.assertTrue(roomRepository.save(room));
    }

    @Test()
    public void readRoom() {
        List<Room> rooms = roomRepository.readAll();
        Assertions.assertEquals(1, rooms.size());
        for (Room r : rooms) {
            System.out.println(r);
        }
    }
}
