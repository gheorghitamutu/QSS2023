package org.application.rooms;

import org.domain.exceptions.room.RoomAdditionException;
import org.domain.exceptions.room.RoomDeletionFailed;
import org.domain.exceptions.room.RoomNotFoundException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Room;

import java.util.List;

public interface IRoomsService {

    public Room addRoom(String name, int capacity, int floor, Room.Type type) throws RoomAdditionException, ValidationException;

    public boolean deleteRoom(int roomId) throws RoomNotFoundException, RoomDeletionFailed, ValidationException;

    public boolean deleteRooms(String name) throws RoomDeletionFailed, ValidationException;

    public boolean deleteAll() throws RoomDeletionFailed;

    public Room getRoomById(int roomId) throws RoomNotFoundException, ValidationException;

    public List<Room> getRooms();
}
