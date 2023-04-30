package org.application.dataaccess.room;

import com.google.inject.Inject;
import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.repository.BaseRepository;
import org.application.domain.models.Room;

public class RoomRepository extends BaseRepository<Room> implements IRoomRepository {

    @Inject
    public RoomRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);
    }

}
