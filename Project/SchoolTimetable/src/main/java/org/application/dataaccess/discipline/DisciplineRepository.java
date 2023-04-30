package org.application.dataaccess.discipline;

import com.google.inject.Inject;
import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.repository.BaseRepository;
import org.application.domain.models.Discipline;

public class DisciplineRepository extends BaseRepository<Discipline> implements IDisciplineRepository {

    @Inject
    public DisciplineRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);
    }

}
