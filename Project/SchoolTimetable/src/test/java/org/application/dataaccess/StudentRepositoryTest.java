package org.application.dataaccess;

import org.application.Application;
import org.application.GuiceInjectorSingleton;
import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.database.TestsDatabaseHibernateProvider;
import org.application.dataaccess.student.StudentRepository;
import org.application.dataaccess.studentgroup.StudentGroupRepository;
import org.application.databaseseed.TimetableEntitiesFactory;
import org.application.di.TestsDI;
import org.application.domain.exceptions.RepositoryOperationException;
import org.application.domain.exceptions.Timeslot.TimeslotDeletionFailed;
import org.application.domain.exceptions.discipline.DisciplineDeletionFailed;
import org.application.domain.exceptions.room.RoomDeletionFailed;
import org.application.domain.exceptions.session.SessionDeletionFailed;
import org.application.domain.exceptions.student.StudentDeletionFailed;
import org.application.domain.exceptions.student.StudentNotFoundException;
import org.application.domain.exceptions.studentgroup.StudentGroupDeletionFailed;
import org.application.domain.exceptions.teacher.TeacherDeletionFailed;
import org.application.domain.models.Student;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class StudentRepositoryTest {

    private StudentRepository studentRepository;
    private StudentGroupRepository studentGroupRepository;
    private Application app;


    @BeforeAll
    void setUpAll() {
        TestsDI.initializeDi();

        IHibernateProvider provider = new TestsDatabaseHibernateProvider();

        studentRepository = new StudentRepository(provider);
        studentGroupRepository = new StudentGroupRepository(provider);

        this.app = GuiceInjectorSingleton.INSTANCE.getInjector().getInstance(Application.class);

        new TimetableEntitiesFactory(this.app).createTimetableEntities();
    }

    @AfterAll
    void tearDownAll() throws TimeslotDeletionFailed, TeacherDeletionFailed, StudentDeletionFailed, StudentGroupDeletionFailed, SessionDeletionFailed, RoomDeletionFailed, DisciplineDeletionFailed, TimeslotDeletionFailed, TeacherDeletionFailed, StudentDeletionFailed, StudentGroupDeletionFailed, SessionDeletionFailed, RoomDeletionFailed, DisciplineDeletionFailed {
        app.roomsService.deleteAll();
        app.sessionsService.deleteAll();
        app.studentsService.deleteAll();
        app.studentGroupsService.deleteAll();
        app.teachersService.deleteAll();
        app.timeslotsService.deleteAll();
        app.disciplinesService.deleteAll();
    }


//    public Student changeStudentGroup(Student student, StudentGroup newGroup)

    @Test
//    @Disabled
    public void Given__StudentRepository__When__changeStudentGroupIsCalled__Then__ReturnStudentWithGroupChanged() throws RepositoryOperationException, StudentNotFoundException {
        var studentGroup = studentGroupRepository.getByGroupName("A2");

        //student with id 310910204006SM000004 is in group A4
        var student = studentRepository.readAll().stream().filter(s -> s.getRegistrationNumber().equals("310910204006SM000004"))
                .toList().get(0);

        studentRepository.changeStudentGroup(student, studentGroup);

        Assertions.assertEquals(student.getGroup().getName(), "A2");
    }

//    public Student deleteStudent(Student student)

    @Test
    public void Given__StudentRepository__When__deleteStudentIsCalled__Then__StudentIsDeleted() throws RepositoryOperationException, StudentNotFoundException {
        var student = studentRepository.readAll().stream().filter(s -> s.getRegistrationNumber().equals("310910204006SM000002"))
                .toList().get(0);

        studentRepository.deleteStudent(student);

        var allStudents = studentRepository.readAll();

        var studentExists = allStudents.stream().anyMatch(s -> s.getRegistrationNumber().equals(student.getRegistrationNumber()));

        Assertions.assertFalse(studentExists);
    }

//    public Student updateStudent(Student student)

    @Test
    public void Given__StudentRepository__When__updateStudentIsCalled__Then__StudentIsUpdated() throws RepositoryOperationException, StudentNotFoundException {
        var student = this.app.studentsService.getStudentByRegistrationNumber("310910204006SM000003");

        student.setName("updated_name");

        studentRepository.updateStudent(student);

        var updatedStudent = studentRepository.readAll().stream().filter(s -> s.getRegistrationNumber().equals(student.getRegistrationNumber()))
                .findFirst().orElse(null);

        Assertions.assertNotNull(updatedStudent);
        Assertions.assertEquals(updatedStudent.getName(), "updated_name");
    }




    @Test
    public void saveStudent() throws RepositoryOperationException {
        var studentGroup = studentGroupRepository.getByGroupName("A1");

        Student student = new Student();
        student.setGroup(studentGroup);
        student.setYear(1);
        student.setName("test_student");
        student.setRegistrationNumber("310910204006SM022000");
        student.setInsertTime(new Date());

        //ASSERT (sa nu fie exceptie by default)
        studentRepository.save(student);
    }

    @Test()
    public void readStudent() {
        List<Student> students = studentRepository.readAll();

        Assertions.assertTrue(students.size() > 1);
    }
}
