package org.application.dataaccess.room;

import org.application.dataaccess.repository.IRepository;
import org.application.domain.exceptions.RepositoryOperationException;
import org.application.domain.models.Discipline;
import org.application.domain.models.Room;

public interface IRoomRepository extends IRepository<Room> {
    public Room getByName(String name) throws RepositoryOperationException;

    public Room createNewRoom(String name, int capacity, int floor, Room.Type type) throws RepositoryOperationException;
}
