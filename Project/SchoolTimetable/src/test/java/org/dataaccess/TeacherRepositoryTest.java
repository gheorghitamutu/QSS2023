package org.dataaccess;

import org.Application;
import org.GuiceInjectorSingleton;
import org.dataaccess.database.IHibernateProvider;
import org.dataaccess.database.TestsDatabaseHibernateProvider;
import org.dataaccess.teacher.TeacherRepository;
import org.databaseseed.TimetableEntitiesFactory;
import org.di.TestsDI;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.Timeslot.TimeslotDeletionFailed;
import org.domain.exceptions.discipline.DisciplineDeletionFailed;
import org.domain.exceptions.room.RoomDeletionFailed;
import org.domain.exceptions.session.SessionDeletionFailed;
import org.domain.exceptions.student.StudentDeletionFailed;
import org.domain.exceptions.studentgroup.StudentGroupDeletionFailed;
import org.domain.exceptions.teacher.TeacherDeletionFailed;
import org.domain.exceptions.validations.ValidationException;
import org.domain.models.Teacher;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TeacherRepositoryTest {
    private TeacherRepository teacherRepository;
    private Application app;

    @BeforeEach
    void setUp() {

    }

    @AfterEach
    void tearDown() {
    }

    @BeforeAll
    void setUpAll() {
        TestsDI.initializeDi();

        IHibernateProvider provider = new TestsDatabaseHibernateProvider();
        teacherRepository = new TeacherRepository(provider);

        this.app = GuiceInjectorSingleton.INSTANCE.getInjector().getInstance(Application.class);

        new TimetableEntitiesFactory(this.app).createTimetableEntities();
    }

    @AfterAll
    void tearDownAll() throws RepositoryOperationException, TimeslotDeletionFailed, TeacherDeletionFailed, StudentDeletionFailed, StudentGroupDeletionFailed, SessionDeletionFailed, RoomDeletionFailed, DisciplineDeletionFailed {
        app.disciplinesService.deleteAll();
        app.roomsService.deleteAll();
        app.sessionsService.deleteAll();
        app.studentGroupsService.deleteAll();
        app.studentsService.deleteAll();
        app.teachersService.deleteAll();
        app.timeslotsService.deleteAll();
    }

//    public Teacher createNewTeacher(String name, Teacher.Type type) throws RepositoryOperationException


    @Test
    public void Given__TeacherRepository__When__createNewTeacher__Then__ReturnsNewTeacher() throws RepositoryOperationException, ValidationException {
        teacherRepository.createNewTeacher("test_9999", Teacher.Type.COLLABORATOR);

        Teacher foundTeacher = teacherRepository.readAll().stream().filter(t -> t.getName().equals("test_9999")).findFirst()
                .orElse(null);

        Assertions.assertNotNull(foundTeacher);
        Assertions.assertEquals("test_9999", foundTeacher.getName());
    }

//    public Teacher getByName(String name) throws RepositoryOperationException

    @Test
    public void Given__TeacherRepository__When__getByNameIsCalledWithExistentName__Then__ReturnsTeacher() throws RepositoryOperationException, ValidationException {
        Teacher foundTeacher = teacherRepository.getByName("Teacher 01");

        Assertions.assertNotNull(foundTeacher);
        Assertions.assertEquals("Teacher 01", foundTeacher.getName());
    }

    @Test
    public void Given__TeacherRepository__When__getByNameIsCalledWithNonExistentName__Then__ReturnsNull() throws RepositoryOperationException, ValidationException {
        Teacher foundTeacher = teacherRepository.getByName("Non existent name");

        Assertions.assertNull(foundTeacher);
    }
    @Test
    public void saveTeacher() throws RepositoryOperationException {
        Teacher teacher = new Teacher();
        teacher.setType(Teacher.Type.COLLABORATOR);
        teacher.setName("DummyTeacher Name");
        teacher.setInsertTime(new Date());

        teacherRepository.save(teacher);
    }

    @Test()
    public void readTeacher() {
        List<Teacher> teachers = teacherRepository.readAll();

        Assertions.assertTrue(teachers.size() > 1);
    }
}
