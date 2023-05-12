package org.application.rooms;

import org.domain.exceptions.room.RoomAdditionException;
import org.domain.exceptions.room.RoomDeletionFailed;
import org.domain.exceptions.room.RoomNotFoundException;
import org.domain.models.Room;

import java.util.List;

public interface IRoomsService {

    public Room addRoom(String name, int capacity, int floor, Room.Type type) throws RoomAdditionException;

    public boolean deleteRoom(int roomId) throws RoomNotFoundException, RoomDeletionFailed;

    public boolean deleteRooms(String name) throws RoomDeletionFailed;

    public boolean deleteAll() throws RoomDeletionFailed;

    public Room getRoomById(int roomId) throws RoomNotFoundException;

    public List<Room> getRooms();
}
