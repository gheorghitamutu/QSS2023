package org.dataaccess.room;

import org.dataaccess.repository.IRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Room;


/**
 * An interface representing a repository for managing Room objects.
 * Extends the IRepository interface with specific methods for Room entities.
 * This interface defines the repository operations that can be performed, specific to room repository.
 */
public interface IRoomRepository extends IRepository<Room> {

    /**
     * Retrieves a Room entity by its name.
     *
     * @param name The name of the Room to retrieve.
     * @return The Room entity with the specified name, or null if not found.
     * @throws RepositoryOperationException If an error occurs during the repository operation.
     * @throws ValidationException If the validation of the Room name fails.
     */
    public Room getByName(String name) throws RepositoryOperationException, ValidationException;

    /**
     * Creates a new Room entity with the specified name, capacity, floor, and type.
     *
     * @param name     The name of the new Room.
     * @param capacity The capacity of the new Room.
     * @param floor    The floor of the new Room.
     * @param type     The type of the new Room.
     * @return The newly created Room entity.
     * @throws RepositoryOperationException If an error occurs during the repository operation.
     * @throws ValidationException If the validation of the Room attributes fails.
     */
    public Room createNewRoom(String name, int capacity, int floor, Room.Type type) throws RepositoryOperationException, ValidationException;
}
