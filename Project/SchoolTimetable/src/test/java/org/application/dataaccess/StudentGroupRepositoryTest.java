package org.application.dataaccess;

import org.application.Application;
import org.application.GuiceInjectorSingleton;
import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.database.TestsDatabaseHibernateProvider;
import org.application.dataaccess.studentgroup.StudentGroupRepository;
import org.application.databaseseed.TimetableEntitiesFactory;
import org.application.di.TestsDI;
import org.application.domain.exceptions.RepositoryOperationException;
import org.application.domain.exceptions.Timeslot.TimeslotDeletionFailed;
import org.application.domain.exceptions.discipline.DisciplineDeletionFailed;
import org.application.domain.exceptions.room.RoomDeletionFailed;
import org.application.domain.exceptions.session.SessionDeletionFailed;
import org.application.domain.exceptions.student.StudentDeletionFailed;
import org.application.domain.exceptions.studentgroup.StudentGroupDeletionFailed;
import org.application.domain.exceptions.teacher.TeacherDeletionFailed;
import org.application.domain.models.StudentGroup;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StudentGroupRepositoryTest {
    private StudentGroupRepository studentGroupRepository;
    private Application app;


    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() throws RepositoryOperationException {

    }

    @BeforeAll
    void setUpAll() {
        TestsDI.initializeDi();

        studentGroupRepository = new StudentGroupRepository(new TestsDatabaseHibernateProvider());

        this.app = GuiceInjectorSingleton.INSTANCE.getInjector().getInstance(Application.class);

        new TimetableEntitiesFactory(this.app).createTimetableEntities();
    }

    @AfterAll
    void tearDownAll() throws TimeslotDeletionFailed, TeacherDeletionFailed, StudentDeletionFailed, StudentGroupDeletionFailed, SessionDeletionFailed, RoomDeletionFailed, DisciplineDeletionFailed {
        app.disciplinesService.deleteAll();
        app.roomsService.deleteAll();
        app.sessionsService.deleteAll();
        app.studentGroupsService.deleteAll();
        app.studentsService.deleteAll();
        app.teachersService.deleteAll();
        app.timeslotsService.deleteAll();
    }


//        public StudentGroup getByGroupName(String groupName) {

    @Test
    public void Given__StudentGroupRepository__When__getByGroupNameIsCalledWithExistentGroup__Then__ShouldReturnANonNullGroup() throws RepositoryOperationException {
        StudentGroup group = studentGroupRepository.getByGroupName("A1");

        Assertions.assertNotNull(group);
        Assertions.assertEquals("A1", group.getName());
    }

    @Test
    public void Given__StudentGroupRepository__When__getByGroupNameIsCalledWithNonExistentGroup__Then__ShouldReturnANullGroup() throws RepositoryOperationException {
        StudentGroup group = studentGroupRepository.getByGroupName("A99");

        Assertions.assertNull(group);
    }


//    public StudentGroup createNewGroup(String groupName) throws RepositoryOperationException {
    @Test
    public void Given__StudentGroupRepository__When__createNewGroupIsCalledWithInvalidGroupName__Then__ShouldThrow() {
        Assertions.assertThrows(RepositoryOperationException.class, () -> {
            studentGroupRepository.createNewGroup("111A1");
        });
    }

    @Test
    public void saveGroup() throws RepositoryOperationException {
        StudentGroup group = new StudentGroup();

        group.setName("A8");
        group.setYear(1);
        group.setType(StudentGroup.Type.BACHELOR);
        group.setInsertTime(new Date());

        studentGroupRepository.save(group);
    }

    @Test()
    public void readGroup() throws RepositoryOperationException {

        List<StudentGroup> groups = studentGroupRepository.readAll();
        Assertions.assertTrue(groups.size() > 1);
    }
}
