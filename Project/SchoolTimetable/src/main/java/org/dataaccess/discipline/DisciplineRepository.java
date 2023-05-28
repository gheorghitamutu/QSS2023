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

/**
 * A repository class for managing Discipline entities within the database.
 * Extends the BaseRepository class with Discipline as the generic type and implements the IDisciplineRepository interface.
 * Contains methods for creating new Discipline entities or retrieving Discipline objects by their name.
 */
public class DisciplineRepository extends BaseRepository<Discipline> implements IDisciplineRepository {


    /**
     * Creates a new instance of DisciplineRepository with the specified Hibernate provider.
     *
     * @param hibernateProvider The Hibernate provider to use for data access.
     */
    @Inject
    public DisciplineRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);

    }


    /**
     * Retrieves a Discipline by its name.
     *
     * @param name The name of the Discipline to retrieve.
     * @return The Discipline with the specified name, or null if not found.
     * @throws RepositoryOperationException If an error occurs during the repository operation.
     * @throws ValidationException          If the provided name is blank.
     */
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


    /**
     * Creates a new Discipline with the given name and credits.
     *
     * @param name    The name of the new Discipline.
     * @param credits The number of credits associated with the new Discipline.
     * @return The newly created Discipline.
     * @throws RepositoryOperationException If an error occurs during the repository operation.
     * @throws ValidationException          If the provided name is blank or the credits are negative.
     */
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
