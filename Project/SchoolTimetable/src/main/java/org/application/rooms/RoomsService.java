package org.application.rooms;

import com.google.inject.Inject;
import org.dataaccess.room.IRoomRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.room.RoomAdditionException;
import org.domain.exceptions.room.RoomDeletionFailed;
import org.domain.exceptions.room.RoomNotFoundException;
import org.domain.models.Room;

import java.text.MessageFormat;
import java.util.List;

public class RoomsService implements IRoomsService {

    private final IRoomRepository roomRepository;

    @Inject
    public RoomsService(IRoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    @Override
    public Room addRoom(String name, int capacity, int floor, Room.Type type) throws RoomAdditionException {
        Room room = null;

        if (name == null || name.isEmpty()) {
            throw new RoomAdditionException("[Rooms Service] Room name is invalid");
        }

        if (capacity <= 0) {
            throw new RoomAdditionException("[Rooms Service] Room capacity is invalid");
        }

        if (type == null) {
            throw new RoomAdditionException("[Rooms Service] Room type is invalid");
        }

        try {
            room = roomRepository.getByName(name);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (room == null) {
            try {
                room = roomRepository.createNewRoom(name, capacity, floor, type);
            } catch (RepositoryOperationException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            roomRepository.save(room);
        } catch (Exception e) {
            throw new RoomAdditionException("[RoomService] Failed adding room!", e);
        }

        return room;
    }

    @Override
    public boolean deleteRoom(int roomId) throws RoomNotFoundException, RoomDeletionFailed {

        if (roomId < 0) {
            throw new RoomNotFoundException("[RoomService] Room id is invalid");
        }

        var room = roomRepository.getById(roomId);
        if (room == null) {
            throw new RoomNotFoundException(MessageFormat.format("[RoomService] Room with id {0} not found.", roomId));
        }

        try {
            roomRepository.delete(room);
        } catch (Exception e) {
            throw new RoomDeletionFailed(" [RoomService] Failed to delete room.", e);
        }

        return true;
    }

    @Override
    public boolean deleteRooms(String name) throws RoomDeletionFailed {

        if (name == null || name.isEmpty()) {
            throw new RoomDeletionFailed("[RoomService] Room name is invalid");
        }

        var rooms = roomRepository.readAll().stream().filter(r -> r.getName().equals(name)).toList();

        try {
            roomRepository.deleteMany(rooms);
        } catch (Exception e) {
            throw new RoomDeletionFailed(" [RoomService] Failed to delete rooms.", e);
        }

        return true;
    }

    @Override
    public boolean deleteAll() throws RoomDeletionFailed {
        try {
            roomRepository.deleteMany(roomRepository.readAll());
        } catch (Exception e) {
            throw new RoomDeletionFailed(" [RoomService] Failed to delete rooms.", e);
        }

        return true;
    }

    @Override
    public Room getRoomById(int roomId) throws RoomNotFoundException {

        if (roomId < 0) {
            throw new RoomNotFoundException("[RoomService] Room id is invalid");
        }

        var room = roomRepository.getById(roomId);
        if (room == null) {
            throw new RoomNotFoundException(MessageFormat.format("[RoomService] Room with id {0} not found.", roomId));
        }
        return room;
    }

    @Override
    public List<Room> getRooms() {
        return roomRepository.readAll();
    }
}
