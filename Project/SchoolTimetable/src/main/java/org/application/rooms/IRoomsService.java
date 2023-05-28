package org.application.rooms;

import org.domain.exceptions.room.RoomAdditionException;
import org.domain.exceptions.room.RoomDeletionFailed;
import org.domain.exceptions.room.RoomNotFoundException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Room;

import java.util.List;

/**
 * Represents a service interface for managing Room entities.
 * This interface defines the service operations that can be performed, specific to room service.
 */
public interface IRoomsService {

    /**
     * Adds a new Room with the given name, capacity, floor and type.
     * @param name The name of the new Room.
     * @param capacity The capacity of the new Room.
     * @param floor The floor of the new Room.
     * @param type The type of the new Room.
     * @return The newly created Room.
     * @throws RoomAdditionException If the Room could not be added.
     * @throws ValidationException If the provided name, capacity, floor or type are invalid.
     */
    public Room addRoom(String name, int capacity, int floor, Room.Type type) throws RoomAdditionException, ValidationException;

    /**
     * Deletes a Room with the given id.
     * @param roomId The id of the Room to delete.
     * @return True if the Room was deleted, false otherwise.
     * @throws RoomNotFoundException If the Room with the given id was not found.
     * @throws RoomDeletionFailed If the Room could not be deleted.
     * @throws ValidationException If the provided id is invalid.
     */
    public boolean deleteRoom(int roomId) throws RoomNotFoundException, RoomDeletionFailed, ValidationException;

    /**
     * Deletes all Rooms with the given name.
     * @param name The name of the Rooms to delete.
     * @return True if the Room was deleted, false otherwise.
     * @throws RoomDeletionFailed If the Room could not be deleted.
     * @throws ValidationException If the provided name is invalid.
     */
    public boolean deleteRooms(String name) throws RoomDeletionFailed, ValidationException;

    /**
     * Deletes all Room entities.
     * @return True if all Room entities were deleted, false otherwise.
     * @throws RoomDeletionFailed If the Room entities could not be deleted.
     */
    public boolean deleteAll() throws RoomDeletionFailed;

    /**
     * Retrieves a Room by its ID.
     * @param roomId The ID of the Room to retrieve.
     * @return The Room with the specified ID.
     * @throws RoomNotFoundException If the Room with the given ID was not found.
     * @throws ValidationException If the provided id is invalid.
     */
    public Room getRoomById(int roomId) throws RoomNotFoundException, ValidationException;

    /**
     * Retrieves all Room entities.
     * @return A list of all Room entities.
     */
    public List<Room> getRooms();
}
