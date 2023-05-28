package org.application.rooms;

import com.google.inject.Inject;
import org.application.helpers.ValidationHelpers;
import org.dataaccess.room.IRoomRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.room.RoomAdditionException;
import org.domain.exceptions.room.RoomDeletionFailed;
import org.domain.exceptions.room.RoomNotFoundException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Room;

import java.text.MessageFormat;
import java.util.List;

/**
 * Represents a service class for managing Room entities.
 * This class defines the service operations that can be performed, specific to room service.
 */
public class RoomsService implements IRoomsService {

    /**
     * The room repository.
     */
    private final IRoomRepository roomRepository;

    /**
     * Initializes a new instance of the {@link RoomsService} class.
     * @param roomRepository The room repository.
     */
    @Inject
    public RoomsService(IRoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

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
    @Override
    public Room addRoom(String name, int capacity, int floor, Room.Type type) throws RoomAdditionException, ValidationException {
        Room room = null;

        ValidationHelpers.requireNotBlank(name, IllegalArgumentException.class, "[RoomService] Room name is invalid", null);
        ValidationHelpers.requirePositive(capacity, IllegalArgumentException.class, "[RoomService] Room capacity is invalid", null);
        ValidationHelpers.requireNotNull(type, IllegalArgumentException.class, "[RoomService] Room type is invalid", null);

        try {
            room = roomRepository.getByName(name);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        if (room == null) {
            try {
                room = roomRepository.createNewRoom(name, capacity, floor, type);
            } catch (RepositoryOperationException | ValidationException e) {
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

    /**
     * Deletes the Room with the given id.
     * @param roomId The id of the Room to delete.
     * @return True if the Room was deleted, false otherwise.
     * @throws RoomNotFoundException If the Room with the given id was not found.
     * @throws RoomDeletionFailed If the Room could not be deleted.
     * @throws ValidationException If the provided id is invalid.
     */
    @Override
    public boolean deleteRoom(int roomId) throws RoomNotFoundException, RoomDeletionFailed, ValidationException {
        ValidationHelpers.requirePositiveOrZero(roomId, IllegalArgumentException.class, "[RoomService] Room id is invalid", null);

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

    /**
     * Deletes the Room with the given name.
     * @param name The name of the Room to delete.
     * @return True if the Room was deleted, false otherwise.
     * @throws RoomDeletionFailed If the Room could not be deleted.
     * @throws ValidationException If the provided name is invalid.
     */
    @Override
    public boolean deleteRooms(String name) throws RoomDeletionFailed, ValidationException {

        ValidationHelpers.requireNotBlank(name, IllegalArgumentException.class, "[RoomService] Room name is invalid", null);


        var rooms = roomRepository.readAll().stream().filter(r -> r.getName().equals(name)).toList();

        try {
            roomRepository.deleteMany(rooms);
        } catch (Exception e) {
            throw new RoomDeletionFailed(" [RoomService] Failed to delete rooms.", e);
        }

        return true;
    }

    /**
     * Deletes all the Rooms.
     * @return True if the Rooms were deleted, false otherwise.
     * @throws RoomDeletionFailed If the Rooms could not be deleted.
     */
    @Override
    public boolean deleteAll() throws RoomDeletionFailed {
        try {
            roomRepository.deleteMany(roomRepository.readAll());
        } catch (Exception e) {
            throw new RoomDeletionFailed(" [RoomService] Failed to delete rooms.", e);
        }

        return true;
    }

    /**
     * Gets the Room with the given id.
     * @param roomId The id of the Room to get.
     * @return The Room with the given id.
     * @throws RoomNotFoundException If the Room with the given id was not found.
     * @throws ValidationException If the provided id is invalid.
     */
    @Override
    public Room getRoomById(int roomId) throws RoomNotFoundException, ValidationException {

        ValidationHelpers.requirePositiveOrZero(roomId, IllegalArgumentException.class, "[RoomService] Room id is invalid", null);

        var room = roomRepository.getById(roomId);
        if (room == null) {
            throw new RoomNotFoundException(MessageFormat.format("[RoomService] Room with id {0} not found.", roomId));
        }
        return room;
    }

    /**
     * Retrieves all the Rooms.
     * @return A list of all the Rooms.
     */
    @Override
    public List<Room> getRooms() {
        return roomRepository.readAll();
    }
}
