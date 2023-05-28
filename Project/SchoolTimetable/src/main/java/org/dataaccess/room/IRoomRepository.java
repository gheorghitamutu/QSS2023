package org.dataaccess.room;

import org.dataaccess.repository.IRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Room;

/**
 * This is the interface for Room.
 */
public interface IRoomRepository extends IRepository<Room> {

    /**
     * This is the method to get a room by name.
     * @param name The name.
     * @return The room.
     * @throws RepositoryOperationException The repository operation exception.
     * @throws ValidationException The validation exception.
     */
    public Room getByName(String name) throws RepositoryOperationException, ValidationException;

    /**
     * This is the method to create a new room.
     * @param name The name.
     * @param capacity The capacity.
     * @param floor The floor.
     * @param type The type.
     * @return The room.
     * @throws RepositoryOperationException The repository operation exception.
     * @throws ValidationException The validation exception.
     */
    public Room createNewRoom(String name, int capacity, int floor, Room.Type type) throws RepositoryOperationException, ValidationException;
}
