package org.dataaccess.room;

import com.google.inject.Inject;
import org.application.helpers.ValidationHelpers;
import org.dataaccess.database.IHibernateProvider;
import org.dataaccess.repository.BaseRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Room;

import java.util.Date;

public class RoomRepository extends BaseRepository<Room> implements IRoomRepository {

    @Inject
    public RoomRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);
    }

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
