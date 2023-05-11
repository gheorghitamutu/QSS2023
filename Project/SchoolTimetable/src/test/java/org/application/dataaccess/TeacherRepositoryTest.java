package org.application.dataaccess;

import org.application.Application;
import org.application.GuiceInjectorSingleton;
import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.database.TestsDatabaseHibernateProvider;
import org.application.dataaccess.teacher.TeacherRepository;
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
import org.application.domain.models.Teacher;
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
    public void Given__TeacherRepository__When__createNewTeacher__Then__ReturnsNewTeacher() throws RepositoryOperationException {
        teacherRepository.createNewTeacher("test_9999", Teacher.Type.COLLABORATOR);

        Teacher foundTeacher = teacherRepository.readAll().stream().filter(t -> t.getName().equals("test_9999")).findFirst()
                .orElse(null);

        Assertions.assertNotNull(foundTeacher);
        Assertions.assertEquals("test_9999", foundTeacher.getName());
    }

//    public Teacher getByName(String name) throws RepositoryOperationException

    @Test
    public void Given__TeacherRepository__When__getByNameIsCalledWithExistentName__Then__ReturnsTeacher() throws RepositoryOperationException {
        Teacher foundTeacher = teacherRepository.getByName("Teacher 01");

        Assertions.assertNotNull(foundTeacher);
        Assertions.assertEquals("Teacher 01", foundTeacher.getName());
    }

    @Test
    public void Given__TeacherRepository__When__getByNameIsCalledWithNonExistentName__Then__ReturnsNull() throws RepositoryOperationException {
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
