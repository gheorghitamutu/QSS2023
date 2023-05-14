package org.services;

import org.Application;
import org.GuiceInjectorSingleton;
import org.application.rooms.IRoomsService;
import org.application.rooms.RoomsService;
import org.dataaccess.room.RoomRepository;
import org.databaseseed.TimetableEntitiesFactory;
import org.di.TestsDI;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.Timeslot.TimeslotDeletionFailed;
import org.domain.exceptions.discipline.DisciplineDeletionFailed;
import org.domain.exceptions.room.RoomAdditionException;
import org.domain.exceptions.room.RoomDeletionFailed;
import org.domain.exceptions.room.RoomNotFoundException;
import org.domain.exceptions.session.SessionDeletionFailed;
import org.domain.exceptions.student.StudentDeletionFailed;
import org.domain.exceptions.studentgroup.StudentGroupDeletionFailed;
import org.domain.exceptions.teacher.TeacherDeletionFailed;
import org.domain.models.Room;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyList;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RoomsServiceTests {
    private Application app;

    @BeforeAll
    void setUpAll() {
        TestsDI.initializeDi();
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

    @Test
    public void Given__RoomsService__When__getRoomsWithAtLeastOneExistentRoom__Then__ReturnTheFoundRoom() {

        var roomRepository = Mockito.mock(RoomRepository.class);

        var room1 = new Room("room1", Room.Type.COURSE, 100, 1);
        var room2 = new Room("room2", Room.Type.COURSE, 100, 1);

        Mockito.when(roomRepository.readAll()).thenReturn(new ArrayList<>() {
            {
                add(room1);
            }

            {
                add(room2);
            }
        });

        IRoomsService roomsService = new RoomsService(roomRepository);

        Assertions.assertEquals(2, roomsService.getRooms().size());
    }

    @Test
    public void Given__RoomsService__When__deleteRoomWithInexistentId__Then__ShouldThrow() {

        var roomRepository = Mockito.mock(RoomRepository.class);

        Mockito.when(roomRepository.getById(1)).thenReturn(null);

        IRoomsService roomsService = new RoomsService(roomRepository);

        Assertions.assertThrows(RoomNotFoundException.class, () -> roomsService.deleteRoom(1));
    }

    @Test
    public void Given__RoomsService__When__deleteAllIsCalledAndRepositoryFails__Then__ShouldWrapExceptionAndThrowItFurther() throws RepositoryOperationException {

        var roomRepository = Mockito.mock(RoomRepository.class);

        var room1 = new Room("room1", Room.Type.COURSE, 100, 1);
        var room2 = new Room("room2", Room.Type.COURSE, 100, 1);

        Mockito.when(roomRepository.readAll()).thenReturn(new ArrayList<>() {
            {
                add(room1);
            }

            {
                add(room2);
            }
        });

        Mockito.doThrow(new RepositoryOperationException("Repo failure")).when(roomRepository).deleteMany(anyList());

        IRoomsService roomsService = new RoomsService(roomRepository);

        Assertions.assertThrows(RoomDeletionFailed.class, roomsService::deleteAll);
    }

    @Test
    public void Given__RoomsService__When__deleteRoomsIsCalledAndRepositoryFails__Then__ShouldWrapExceptionAndThrowItFurther() throws RepositoryOperationException {

        var roomRepository = Mockito.mock(RoomRepository.class);

        var room1 = new Room("room1", Room.Type.COURSE, 100, 1);
        var room2 = new Room("room2", Room.Type.COURSE, 100, 1);

        Mockito.when(roomRepository.readAll()).thenReturn(new ArrayList<>() {
            {
                add(room1);
            }

            {
                add(room2);
            }
        });

        Mockito.doThrow(new RepositoryOperationException("Repo failure")).when(roomRepository).deleteMany(anyList());

        IRoomsService roomsService = new RoomsService(roomRepository);

        Assertions.assertThrows(RoomDeletionFailed.class, () -> roomsService.deleteRooms("room1"));
    }

    @Test
    public void Given__RoomsService__When__getRoomByIdIsCalledAndRoomIsNotFound__Then__ShouldThrow() {

        var roomRepository = Mockito.mock(RoomRepository.class);

        var room1 = new Room("room1", Room.Type.COURSE, 100, 1);

        room1.setId(1);

        Mockito.when(roomRepository.getById(1)).thenReturn(null);

        IRoomsService roomsService = new RoomsService(roomRepository);

        Assertions.assertThrows(RoomNotFoundException.class, () -> roomsService.getRoomById(1));
    }

    @Test
    public void Given__RoomsService__When__addRoomIsCalledAndRepositorySaveThrows__Then__ShouldWrapExceptionAndThrowAsWell() throws RepositoryOperationException {

        var roomRepository = Mockito.mock(RoomRepository.class);

        Mockito.doThrow(new RepositoryOperationException("")).when(roomRepository).save(Mockito.any(Room.class));

        Mockito.when(roomRepository.getByName("room1")).thenReturn(new Room("room1", Room.Type.COURSE, 100, 1));


        Assertions.assertThrows(RoomAdditionException.class, () -> {
            IRoomsService roomsService = new RoomsService(roomRepository);

            var room = roomsService.addRoom("room1", 100, 1, Room.Type.COURSE);
        });
    }

    @Test
    public void Given__RoomsService__When__addRoomIsCalledAndTargetRoomExists__Then__ShouldReturnTheFoundRoom() throws RoomAdditionException, RepositoryOperationException {

        var roomRepository = Mockito.mock(RoomRepository.class);

        Mockito.when(roomRepository.getByName("room1")).thenReturn(new Room("room1", Room.Type.COURSE, 100, 1));

        Mockito.when(roomRepository.createNewRoom("room1", 50, 2, Room.Type.LABORATORY)).thenReturn(null);

        IRoomsService roomsService = new RoomsService(roomRepository);

        var room = roomsService.addRoom("room1", 50, 2, Room.Type.LABORATORY);

        Assertions.assertEquals(room.getType(), Room.Type.COURSE);
        Assertions.assertEquals(room.getName(), "room1");
        Assertions.assertEquals(room.getCapacity(), 100);
        Assertions.assertEquals(room.getFloor(), 1);
    }
}