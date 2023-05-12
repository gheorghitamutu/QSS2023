package org.dataaccess.room;

import org.dataaccess.repository.IRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.models.Room;

public interface IRoomRepository extends IRepository<Room> {
    public Room getByName(String name) throws RepositoryOperationException;

    public Room createNewRoom(String name, int capacity, int floor, Room.Type type) throws RepositoryOperationException;
}
