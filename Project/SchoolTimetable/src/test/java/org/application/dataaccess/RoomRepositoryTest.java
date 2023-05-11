package org.application.dataaccess;

import org.application.Application;
import org.application.GuiceInjectorSingleton;
import org.application.dataaccess.database.TestsDatabaseHibernateProvider;
import org.application.dataaccess.room.RoomRepository;
import org.application.databaseseed.TimetableEntitiesFactory;
import org.application.di.TestsDI;
import org.application.domain.exceptions.RepositoryOperationException;
import org.application.domain.exceptions.Timeslot.TimeslotDeletionFailed;
import org.application.domain.exceptions.discipline.DisciplineDeletionFailed;
import org.application.domain.exceptions.room.RoomDeletionFailed;
import org.application.domain.exceptions.session.SessionDeletionFailed;
import org.application.domain.exceptions.student.StudentDeletionFailed;
import org.application.domain.exceptions.studentgroup.StudentGroupDeletionFailed;
import org.application.domain.exceptions.teacher.TeacherDeletionFailed;
import org.application.domain.models.Room;
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
