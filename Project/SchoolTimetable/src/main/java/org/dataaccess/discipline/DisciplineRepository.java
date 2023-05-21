package org.dataaccess.discipline;

import com.google.inject.Inject;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import org.application.helpers.ValidationHelpers;
import org.dataaccess.database.IHibernateProvider;
import org.dataaccess.repository.BaseRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Discipline;
import org.hibernate.validator.messageinterpolation.ParameterMessageInterpolator;

import java.util.Date;
import java.util.Objects;

public class DisciplineRepository extends BaseRepository<Discipline> implements IDisciplineRepository {


    @Inject
    public DisciplineRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);

    }

    @Override
    public Discipline getByName(String name) throws RepositoryOperationException, ValidationException {

        ValidationHelpers.requireNotBlank(name, IllegalArgumentException.class, "Discipline name cannot be blank.", null);

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
    public Discipline createNewDiscipline(String name, int credits) throws RepositoryOperationException, ValidationException {

        ValidationHelpers.requireNotBlank(name, IllegalArgumentException.class, "Discipline name cannot be blank.", null);
        ValidationHelpers.requirePositiveOrZero(credits, IllegalArgumentException.class, "Discipline credits cannot be negative.", null);

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
