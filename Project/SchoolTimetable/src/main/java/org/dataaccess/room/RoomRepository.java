package org.dataaccess.room;

import com.google.inject.Inject;
import org.application.helpers.ValidationHelpers;
import org.dataaccess.database.IHibernateProvider;
import org.dataaccess.repository.BaseRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Room;

import java.util.Date;


/**
 * A repository class for managing Room entities within the database.
 * Extends the BaseRepository of type Room and implements the IRoomRepository interface.
 * Contains methods for creating new Room entity or retrieving Room objects by their name.
 */
public class RoomRepository extends BaseRepository<Room> implements IRoomRepository {

    /**
     * Constructs a new RoomRepository instance with the specified Hibernate provider.
     *
     * @param hibernateProvider The Hibernate provider to be used for data access.
     */
    @Inject
    public RoomRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);
    }

    /**
     * Retrieves a Room object by its name.
     *
     * @param name The name of the Room to retrieve.
     * @return The Room entity with the specified name, or null if not found.
     * @throws RepositoryOperationException If an error occurs during the repository operation.
     * @throws ValidationException If the validation of the Room name fails.
     */
    public Room getByName(String name) throws RepositoryOperationException, ValidationException {


        ValidationHelpers.requireNotBlank(name, IllegalArgumentException.class, "Room name cannot be blank.", null);

        var session = hibernateProvider.getEntityManager();
        var query = session.createNamedQuery("Room.getByName", Room.class);
        query.setParameter("name", name);

        var result = query.getResultList();
        if (result.size() == 0) {
            return null;
        }

        return result.get(0);
    }

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
    public Room createNewRoom(String name, int capacity, int floor, Room.Type type) throws RepositoryOperationException, ValidationException {


        ValidationHelpers.requireNotBlank(name, IllegalArgumentException.class, "Room name cannot be blank.", null);
        ValidationHelpers.requirePositiveOrZero(capacity, IllegalArgumentException.class, "Room capacity cannot be negative.", null);

        ValidationHelpers.requireNotNull(type, IllegalArgumentException.class, "Room type cannot be null.", null);

        var room = new Room();
        room.setName(name);
        room.setCapacity(capacity);
        room.setFloor(floor);
        room.setType(type);
        room.setInsertTime(new Date());

        try {
            save(room);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RepositoryOperationException("[RoomRepository] Couldn't create new room.", e);
        }

        return room;
    }
}
