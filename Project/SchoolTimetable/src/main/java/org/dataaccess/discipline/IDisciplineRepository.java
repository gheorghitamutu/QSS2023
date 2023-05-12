package org.dataaccess.discipline;

import org.dataaccess.repository.IRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.models.Discipline;

public interface IDisciplineRepository extends IRepository<Discipline> {

    public Discipline getByName(String name) throws RepositoryOperationException;
    public Discipline createNewDiscipline(String name, int credits) throws RepositoryOperationException;
}
