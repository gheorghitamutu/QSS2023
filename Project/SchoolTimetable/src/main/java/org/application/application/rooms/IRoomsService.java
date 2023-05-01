package org.application.application.rooms;

import org.application.domain.exceptions.room.RoomAdditionException;
import org.application.domain.exceptions.room.RoomDeletionFailed;
import org.application.domain.exceptions.room.RoomNotFoundException;
import org.application.domain.models.Room;

import java.util.List;

public interface IRoomsService {

    public Room addRoom(String name, int capacity, int floor, Room.Type type) throws RoomAdditionException;

    public boolean deleteRoom(int roomId) throws RoomNotFoundException, RoomDeletionFailed;

    public Room getRoomById(int roomId) throws RoomNotFoundException;

    public List<Room> getRooms();
}
