package org.application.dataaccess.discipline;

import org.application.dataaccess.repository.IRepository;
import org.application.domain.exceptions.RepositoryOperationException;
import org.application.domain.models.Discipline;
import org.application.domain.models.StudentGroup;

public interface IDisciplineRepository extends IRepository<Discipline> {

    public Discipline getByName(String name) throws RepositoryOperationException;
    public Discipline createNewDiscipline(String name, int credits) throws RepositoryOperationException;
}
