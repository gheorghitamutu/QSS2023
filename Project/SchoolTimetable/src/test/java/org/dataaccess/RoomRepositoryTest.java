package org.dataaccess;

import org.Application;
import org.GuiceInjectorSingleton;
import org.dataaccess.database.TestsDatabaseHibernateProvider;
import org.dataaccess.room.RoomRepository;
import org.databaseseed.TimetableEntitiesFactory;
import org.di.TestsDI;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.Timeslot.TimeslotDeletionFailed;
import org.domain.exceptions.discipline.DisciplineDeletionFailed;
import org.domain.exceptions.room.RoomDeletionFailed;
import org.domain.exceptions.session.SessionDeletionFailed;
import org.domain.exceptions.student.StudentDeletionFailed;
import org.domain.exceptions.studentgroup.StudentGroupDeletionFailed;
import org.domain.exceptions.teacher.TeacherDeletionFailed;
import org.domain.models.Room;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RoomRepositoryTest {

    private RoomRepository roomRepository;
    private Application app;


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @BeforeAll
    void setUpAll() {
        TestsDI.initializeDi();

        this.app = GuiceInjectorSingleton.INSTANCE.getInjector().getInstance(Application.class);
        new TimetableEntitiesFactory(app).createTimetableEntities();

        roomRepository = new RoomRepository(new TestsDatabaseHibernateProvider());
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
    public void Given__RoomRepository__When__getByNameIsCalledWithExistentRoomName__Then__ReturnNotNullRoomWithThatName() throws RepositoryOperationException {
        Room room = roomRepository.getByName("C100");
        Assertions.assertNotNull(room);
    }

    @Test
    public void Given__RoomRepository__When__getByNameIsCalledWithInexistentRoomName__Then__ReturnNotNullRoomWithThatName() throws RepositoryOperationException {
        Room room = roomRepository.getByName("C99999");
        Assertions.assertNull(room);
    }

    @Test
    public void Given__RoomRepository__When__createNewRoomIsCalled__Then__CreateAndPersistThatRoom() throws RepositoryOperationException {
        Room room = new Room();
        room.setCapacity(30);
        room.setFloor(1);
        room.setName("test_C309");
        room.setType(Room.Type.COURSE);
        room.setInsertTime(new Date());

        roomRepository.save(room);

        Assertions.assertNotNull(roomRepository.getByName("test_C309"));
    }


    @Test
    public void saveRoom() throws RepositoryOperationException {
        Room room = new Room();
        room.setCapacity(30);
        room.setFloor(1);
        room.setName("test");
        room.setType(Room.Type.COURSE);
        room.setInsertTime(new Date());

        roomRepository.save(room);
    }

    @Test()
    public void readRoom() {
        List<Room> rooms = roomRepository.readAll();

        Assertions.assertTrue(rooms.size() > 1);
        for (Room r : rooms) {
            System.out.println(r);
        }
    }
}
