package org.dataaccess.room;

import com.google.inject.Inject;
import org.dataaccess.database.IHibernateProvider;
import org.dataaccess.repository.BaseRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.models.Room;

import java.util.Date;

public class RoomRepository extends BaseRepository<Room> implements IRoomRepository {

    @Inject
    public RoomRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);
    }

    public Room getByName(String name) throws RepositoryOperationException {

        if (name == null) {
            throw new RepositoryOperationException("[RoomRepository Validation] Name cannot be null.");
        }

        var session = hibernateProvider.getEntityManager();
        var query = session.createNamedQuery("Room.getByName", Room.class);
        query.setParameter("name", name);

        var result = query.getResultList();
        if (result.size() == 0) {
            return null;
        }

        return result.get(0);
    }

    public Room createNewRoom(String name, int capacity, int floor, Room.Type type) throws RepositoryOperationException  {

        if (name == null) {
            throw new RepositoryOperationException("[RoomRepository Validation] Name cannot be null.");
        }

        if (capacity <= 0) {
            throw new RepositoryOperationException("[RoomRepository Validation] Capacity cannot be negative.");
        }

        if (type == null) {
            throw new RepositoryOperationException("[RoomRepository Validation] Type cannot be null.");
        }

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
