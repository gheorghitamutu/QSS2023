package org.application.dataaccess.room;

import com.google.inject.Inject;
import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.repository.BaseRepository;
import org.application.domain.exceptions.RepositoryOperationException;
import org.application.domain.models.Discipline;
import org.application.domain.models.Room;

import java.util.Date;

public class RoomRepository extends BaseRepository<Room> implements IRoomRepository {

    @Inject
    public RoomRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);
    }

    public Room getByName(String name) throws RepositoryOperationException {
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
