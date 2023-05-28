package org.dataaccess.studentgroup;

import com.google.inject.Inject;
import org.application.helpers.ValidationHelpers;
import org.dataaccess.database.IHibernateProvider;
import org.dataaccess.repository.BaseRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.StudentGroup;

import java.util.Date;


/**
 * A repository class for managing StudentGroup entities.
 * Extends the BaseRepository class with StudentGroup as the generic type and implements the IStudentGroupRepository interface.
 * Contains methods for retrieving StudentGroup objects by their group name and creating new StudentGroup objects.
 */
public class StudentGroupRepository extends BaseRepository<StudentGroup> implements IStudentGroupRepository {

    /**
     * Constructs a new StudentGroupRepository with the specified Hibernate provider.
     *
     * @param hibernateProvider The Hibernate provider to be used for database operations.
     */
    @Inject
    public StudentGroupRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);
    }


    /**
     * Retrieves a StudentGroup entity by its group name.
     *
     * @param groupName The name of the group to retrieve.
     * @return The StudentGroup entity with the specified group name, or null if not found.
     * @throws RepositoryOperationException If an error occurs during the repository operation.
     * @throws ValidationException If the validation of the group name fails.
     */
    @Override
    public StudentGroup getByGroupName(String groupName) throws RepositoryOperationException, ValidationException {

        ValidationHelpers.requireNotBlank(groupName, IllegalArgumentException.class, "Group name cannot be blank.", null);

        //regex for group name is enforced so if groupname is invalid => no results

        var session = hibernateProvider.getEntityManager();
        var query = session.createNamedQuery("StudentGroup.getByGroupName", StudentGroup.class);
        query.setParameter("name", groupName);

        var result = query.getResultList();
        if (result.size() == 0) {
            return null;
        }

        return result.get(0);
    }


    /**
     * Creates a new StudentGroup entity with the specified group name.
     *
     * @param groupName The name of the new group.
     * @return The newly created StudentGroup entity.
     * @throws RepositoryOperationException If an error occurs during the repository operation.
     * @throws ValidationException If the validation of the group name fails.
     */
    @Override
    public StudentGroup createNewGroup(String groupName) throws RepositoryOperationException, ValidationException {

        ValidationHelpers.requireNotBlank(groupName, IllegalArgumentException.class, "Group name cannot be blank.", null);
        ValidationHelpers.requireMatchesRegex(groupName, "[A-Z]{1}[0-9]{1}", IllegalArgumentException.class, "Group name must match regex [A-Z]{1}[0-9]{1}.", null);

        StudentGroup studentGroup = new StudentGroup();

        studentGroup.setName(groupName);
        studentGroup.setYear(1);
        studentGroup.setType(StudentGroup.Type.BACHELOR);
        studentGroup.setInsertTime(new Date());

        try {
            save(studentGroup);
            return studentGroup;
        } catch (Exception e) {
            e.printStackTrace();

            throw new RepositoryOperationException("[Student Group Repository] Couldn't create new group.", e);
        }
    }
}
