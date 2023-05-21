package org.dataaccess.discipline;

import org.dataaccess.repository.IRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Discipline;

public interface IDisciplineRepository extends IRepository<Discipline> {

    public Discipline getByName(String name) throws RepositoryOperationException, ValidationException;
    public Discipline createNewDiscipline(String name, int credits) throws RepositoryOperationException, ValidationException;
}
