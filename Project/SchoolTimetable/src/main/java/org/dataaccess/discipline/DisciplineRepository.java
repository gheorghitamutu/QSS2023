package org.dataaccess.discipline;

import com.google.inject.Inject;
import org.dataaccess.database.IHibernateProvider;
import org.dataaccess.repository.BaseRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.models.Discipline;

import java.util.Date;

public class DisciplineRepository extends BaseRepository<Discipline> implements IDisciplineRepository {

    @Inject
    public DisciplineRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);
    }

    @Override
    public Discipline getByName(String name) throws RepositoryOperationException {
        var session = hibernateProvider.getEntityManager();
        var query = session.createNamedQuery("Discipline.getByName", Discipline.class);
        query.setParameter("name", name);

        var result = query.getResultList();
        if (result.size() == 0) {
            return null;
        }

        return result.get(0);
    }

    @Override
    public Discipline createNewDiscipline(String name, int credits) throws RepositoryOperationException {

        var discipline = new Discipline();
        discipline.setName(name);
        discipline.setCredits(credits);
        discipline.setInsertTime(new Date());

        try {
            save(discipline);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RepositoryOperationException("[DisciplineRepository] Couldn't create new discipline.", e);
        }

        return discipline;
    }
}
