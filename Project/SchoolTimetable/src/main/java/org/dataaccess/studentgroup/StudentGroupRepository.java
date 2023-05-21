package org.dataaccess.studentgroup;

import com.google.inject.Inject;
import org.application.helpers.ValidationHelpers;
import org.dataaccess.database.IHibernateProvider;
import org.dataaccess.repository.BaseRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.StudentGroup;

import java.util.Date;

public class StudentGroupRepository extends BaseRepository<StudentGroup> implements IStudentGroupRepository {

    @Inject
    public StudentGroupRepository(IHibernateProvider hibernateProvider) {
        super(hibernateProvider);
    }

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
