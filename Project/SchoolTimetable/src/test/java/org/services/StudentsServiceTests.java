package org.services;

import org.GuiceInjectorSingleton;
import org.Application;
import org.application.students.IStudentsService;
import org.application.students.StudentsService;
import org.dataaccess.student.StudentRepository;
import org.dataaccess.studentgroup.StudentGroupRepository;
import org.databaseseed.TimetableEntitiesFactory;
import org.di.TestsDI;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.Timeslot.TimeslotDeletionFailed;
import org.domain.exceptions.discipline.DisciplineDeletionFailed;
import org.domain.exceptions.room.RoomDeletionFailed;
import org.domain.exceptions.session.SessionDeletionFailed;
import org.domain.exceptions.student.StudentAdditionException;
import org.domain.exceptions.student.StudentDeletionFailed;
import org.domain.exceptions.student.StudentNotFoundException;
import org.domain.exceptions.student.StudentUpdateException;
import org.domain.exceptions.studentgroup.StudentGroupDeletionFailed;
import org.domain.exceptions.studentgroup.StudentGroupReassignException;
import org.domain.exceptions.teacher.TeacherDeletionFailed;
import org.domain.models.Student;
import org.domain.models.StudentGroup;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.*;

import static org.mockito.ArgumentMatchers.anyList;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentsServiceTests {

    private Application app;

    @BeforeAll
    void setUpAll() {
        TestsDI.initializeDi();
        this.app = GuiceInjectorSingleton.INSTANCE.getInjector().getInstance(Application.class);
        new TimetableEntitiesFactory(app).createTimetableEntities();
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

//public List<Student> getStudentsByGroupNameAndYear(String groupName, int year)

    @Test
    public void Given__StudentsService__When__getStudentsByGroupNameAndYearWithAtLeastOneExistentStudent__Then__ReturnTheFoundStudents() {


        //mock IStudentsGroupRepository
        //mock IStudentsRepository

        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);
        var studentRepository = Mockito.mock(StudentRepository.class);

        var student1 = new Student("student1", 1, "301401RSL191122");
        var student2 = new Student("student2", 1, "301401RSL191123");

        var studentGroup = new StudentGroup("A1", 1, StudentGroup.Type.BACHELOR);

        student1.setGroup(studentGroup);
        student2.setGroup(studentGroup);

        studentGroup.setStudents(new HashSet<>(Arrays.asList(student1, student2)));


        Mockito.when(studentGroupRepository.readAll())
                .thenReturn(new ArrayList<>() {
                    {
                        add(studentGroup);
                    }
                });


        IStudentsService studentsService = new StudentsService(studentRepository, studentGroupRepository);

        Assertions.assertEquals(2, studentsService.getStudentsByGroupNameAndYear("A1", 1).size());
    }

//public List<Student> getStudents()

    @Test
    public void Given__StudentsService__When__getStudentsWithAtLeastOneExistentStudent__Then__ReturnTheFoundStudents() {

        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);
        var studentRepository = Mockito.mock(StudentRepository.class);

        var student1 = new Student("student1", 1, "301401RSL191122");
        var student2 = new Student("student2", 1, "301401RSL191123");

        var studentGroup = new StudentGroup("A1", 1, StudentGroup.Type.BACHELOR);

        student1.setGroup(studentGroup);
        student2.setGroup(studentGroup);

        studentGroup.setStudents(new HashSet<>(Arrays.asList(student1, student2)));

        Mockito.when(studentRepository.readAll())
                .thenReturn(new ArrayList<>() {
                    {
                        add(student1);
                    }

                    {
                        add(student2);
                    }
                });

        IStudentsService studentsService = new StudentsService(studentRepository, studentGroupRepository);

        Assertions.assertEquals(2, studentsService.getStudents().size());
    }


//public boolean deleteStudent(String registrationNumber) throws StudentNotFoundException, StudentDeletionFailed

    @Test
    public void Given__StudentsService__When__deleteStudentWithInexistentStudent__Then__ShouldThrow() {

        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);
        var studentRepository = Mockito.mock(StudentRepository.class);

        var student1 = new Student("student1", 1, "301401RSL191122");
        var student2 = new Student("student2", 1, "301401RSL191123");

        var studentGroup = new StudentGroup("A1", 1, StudentGroup.Type.BACHELOR);

        student1.setGroup(studentGroup);
        student2.setGroup(studentGroup);

        studentGroup.setStudents(new HashSet<>(Arrays.asList(student1, student2)));


        Mockito.when(studentRepository.readAll())
                .thenReturn(new ArrayList<>() {
                    {
                        add(student1);
                    }

                    {
                        add(student2);
                    }
                });

        IStudentsService studentsService = new StudentsService(studentRepository, studentGroupRepository);

        Assertions.assertThrows(StudentNotFoundException.class, () -> studentsService.deleteStudent("301401RSL191124"));
    }


    @Test
    public void Given__StudentsService__When__deleteStudentIsCalledAndMultipleMatchingStudentsWithThatRegistrationNumberAreFound__Then__ShouldThrow() {


        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);
        var studentRepository = Mockito.mock(StudentRepository.class);

        var student1 = new Student("student1", 1, "301401RSL191122");
        var student2 = new Student("student2", 1, "301401RSL191123");
        var student3 = new Student("student2", 1, "301401RSL191123");

        var studentGroup = new StudentGroup("A1", 1, StudentGroup.Type.BACHELOR);

        student1.setGroup(studentGroup);
        student2.setGroup(studentGroup);
        student3.setGroup(studentGroup);

        studentGroup.setStudents(new HashSet<>(Arrays.asList(student1, student2)));


        Mockito.when(studentRepository.readAll())
                .thenReturn(new ArrayList<>() {
                    {
                        add(student1);
                        add(student2);
                        add(student3);
                    }
                });

        IStudentsService studentsService = new StudentsService(studentRepository, studentGroupRepository);

        Assertions.assertThrows(StudentNotFoundException.class, () -> studentsService.deleteStudent("301401RSL191124"));
    }


//public boolean deleteAll() throws StudentDeletionFailed

    @Test
    public void Given__StudentsService__When__deleteAllIsCalledAndRepositoryFails__Then__ShouldWrapExceptionAndThrowItFurther() throws RepositoryOperationException {

        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);
        var studentRepository = Mockito.mock(StudentRepository.class);

        var student1 = new Student("student1", 1, "301401RSL191122");
        var student2 = new Student("student2", 1, "301401RSL191123");

        var studentGroup = new StudentGroup("A1", 1, StudentGroup.Type.BACHELOR);

        student1.setGroup(studentGroup);
        student2.setGroup(studentGroup);

        studentGroup.setStudents(new HashSet<>(Arrays.asList(student1, student2)));

        Mockito.when(studentRepository.readAll())
                .thenReturn(new ArrayList<>() {
                    {
                        add(student1);
                    }

                    {
                        add(student2);
                    }
                });

        Mockito.doThrow(new RepositoryOperationException("Repo failure")).when(studentRepository).deleteMany(anyList());

        IStudentsService studentsService = new StudentsService(studentRepository, studentGroupRepository);

        Assertions.assertThrows(StudentDeletionFailed.class, () -> studentsService.deleteAll());
    }


//public Student getStudentByRegistrationNumber(String registrationNumber) throws StudentNotFoundException

    @Test
    public void Given__StudentsService__When__getStudentByRegistrationNumberIsCalledAndStudentIsFound__Then__ShouldReturnTheFoundStudent() throws StudentNotFoundException {

        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);
        var studentRepository = Mockito.mock(StudentRepository.class);

        var student1 = new Student("student1", 1, "301401RSL191122");
        var student2 = new Student("student2", 1, "301401RSL191123");

        var studentGroup = new StudentGroup("A1", 1, StudentGroup.Type.BACHELOR);

        student1.setGroup(studentGroup);
        student2.setGroup(studentGroup);

        studentGroup.setStudents(new HashSet<>(Arrays.asList(student1, student2)));


        Mockito.when(studentRepository.readAll())
                .thenReturn(new ArrayList<>() {
                    {
                        add(student1);
                    }

                    {
                        add(student2);
                    }
                });

        IStudentsService studentsService = new StudentsService(studentRepository, studentGroupRepository);

        Assertions.assertEquals(student1.getRegistrationNumber(), studentsService.getStudentByRegistrationNumber("301401RSL191122").getRegistrationNumber());
    }

    @Test
    public void Given__StudentsService__When__getStudentByRegistrationNumberIsCalledAndStudentIsNotFound__Then__ShouldThrow() throws StudentNotFoundException {

        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);
        var studentRepository = Mockito.mock(StudentRepository.class);

        var student1 = new Student("student1", 1, "301401RSL191122");
        var student2 = new Student("student2", 1, "301401RSL191123");

        var studentGroup = new StudentGroup("A1", 1, StudentGroup.Type.BACHELOR);

        student1.setGroup(studentGroup);
        student2.setGroup(studentGroup);

        studentGroup.setStudents(new HashSet<>(Arrays.asList(student1, student2)));


        Mockito.when(studentRepository.readAll())
                .thenReturn(new ArrayList<>() {
                    {
                        add(student1);
                    }

                    {
                        add(student2);
                    }
                });

        IStudentsService studentsService = new StudentsService(studentRepository, studentGroupRepository);

        Assertions.assertThrows(StudentNotFoundException.class, () -> studentsService.getStudentByRegistrationNumber("301401RSL191124"));
    }


//public Student getStudentById(int studentId) throws StudentNotFoundException

    @Test
    public void Given__StudentsService__When__getStudentByIdIsCalledAndStudentIsFound__Then__ShouldReturnTheFoundStudent() throws StudentNotFoundException {

        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);
        var studentRepository = Mockito.mock(StudentRepository.class);

        var student1 = new Student("student1", 1, "301401RSL191122");
        var student2 = new Student("student2", 1, "301401RSL191123");

        student1.setId(1);
        student2.setId(2);

        var studentGroup = new StudentGroup("A1", 1, StudentGroup.Type.BACHELOR);

        student1.setGroup(studentGroup);
        student2.setGroup(studentGroup);

        studentGroup.setStudents(new HashSet<>(Arrays.asList(student1, student2)));


        Mockito.when(studentRepository.getById(1))
                .thenReturn(student1);

        IStudentsService studentsService = new StudentsService(studentRepository, studentGroupRepository);

        Assertions.assertEquals(student1.getRegistrationNumber(), studentsService.getStudentById(1).getRegistrationNumber());
    }

    @Test
    public void Given__StudentsService__When__getStudentByIdIsCalledAndStudentIsNotFound__Then__ShouldThrow() throws StudentNotFoundException {

        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);
        var studentRepository = Mockito.mock(StudentRepository.class);

        var student1 = new Student("student1", 1, "301401RSL191122");
        var student2 = new Student("student2", 1, "301401RSL191123");


        var studentGroup = new StudentGroup("A1", 1, StudentGroup.Type.BACHELOR);

        student1.setGroup(studentGroup);
        student2.setGroup(studentGroup);

        studentGroup.setStudents(new HashSet<>(Arrays.asList(student1, student2)));

        student1.setId(1);
        student2.setId(2);

        Mockito.when(studentRepository.readAll())
                .thenReturn(new ArrayList<>() {
                    {
                        add(student1);
                    }

                    {
                        add(student2);
                    }
                });

        IStudentsService studentsService = new StudentsService(studentRepository, studentGroupRepository);

        Assertions.assertThrows(StudentNotFoundException.class, () -> studentsService.getStudentById(3));
    }


//public Student reassignStudent(int studentId, String newGroupName) throws StudentGroupReassignException

    @Test
    public void Given__StudentsService__When__reassignStudentIsCalledAndStudentIsFoundWhichIsAlreadyInAGroup__Then__ShouldReturnTheFoundStudentWithDifferentGroup() throws StudentGroupReassignException, RepositoryOperationException {

        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);
        var studentRepository = Mockito.mock(StudentRepository.class);

        var student1 = new Student("student1", 1, "301401RSL191122");

        var studentGroup = new StudentGroup("A1", 1, StudentGroup.Type.BACHELOR);

        student1.setGroup(studentGroup);

        studentGroup.setStudents(new HashSet<>(List.of(student1)));

        student1.setId(1);

        Mockito.when(studentRepository.getById(1))
                .thenReturn(student1);

        Mockito.when(studentGroupRepository.getByGroupName("A2"))
                .thenReturn(new StudentGroup("A2", 1, StudentGroup.Type.BACHELOR));

        IStudentsService studentsService = new StudentsService(studentRepository, studentGroupRepository);

        studentsService.reassignStudent(1, "A2");

        Assertions.assertEquals(student1.getGroup().getName(), "A2");
    }


    @Test
    public void Given__StudentsService__When__reassignStudentIsCalledWithAStudentThatIsFoundAndHasNoGroup__Then__ShouldReturnTheFoundStudentWithTheNewlyProvidedGroup() throws StudentGroupReassignException, RepositoryOperationException {

        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);
        var studentRepository = Mockito.mock(StudentRepository.class);

        var student1 = new Student("student1", 1, "301401RSL191122");

        student1.setId(1);

        Mockito.when(studentRepository.getById(1))
                .thenReturn(student1);

        Mockito.when(studentGroupRepository.getByGroupName("A2"))
                .thenReturn(new StudentGroup("A2", 1, StudentGroup.Type.BACHELOR));

        IStudentsService studentsService = new StudentsService(studentRepository, studentGroupRepository);

        studentsService.reassignStudent(1, "A2");

        Assertions.assertEquals(student1.getGroup().getName(), "A2");
    }

    @Test
    public void Given__StudentsService__When__reassignStudentIsCalledWithAStudentThatIsFoundAndHasNoGroupAndTargetGroupDoesntExist__Then__ShouldReturnTheFoundStudentWithTheNewlyProvidedGroupAfterTheGroupWasCreated() throws StudentGroupReassignException, RepositoryOperationException {

        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);
        var studentRepository = Mockito.mock(StudentRepository.class);

        var student1 = new Student("student1", 1, "301401RSL191122");

        student1.setId(1);

        Mockito.when(studentRepository.getById(1))
                .thenReturn(student1);

        Mockito.when(studentGroupRepository.getByGroupName("A2"))
                .thenReturn(null);

        Mockito.when(studentGroupRepository.createNewGroup("A2"))
                .thenReturn(new StudentGroup("A2", 1, StudentGroup.Type.BACHELOR));

        IStudentsService studentsService = new StudentsService(studentRepository, studentGroupRepository);

        studentsService.reassignStudent(1, "A2");

        Assertions.assertEquals(student1.getGroup().getName(), "A2");
    }

//public Student addStudent(String name, String registrationNumber, int year, String groupName) throws StudentAdditionException

    @Test
    public void Given__StudentsService__When__addStudentIsCalledAndTargetGroupDoesntExist__Then__ShouldReturnTheFoundStudentWithTheNewlyCreatedGroup() throws StudentAdditionException, RepositoryOperationException {

        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);
        var studentRepository = Mockito.mock(StudentRepository.class);


        Mockito.when(studentGroupRepository.getByGroupName("A2"))
                .thenReturn(null);

        Mockito.when(studentGroupRepository.createNewGroup("A2"))
                .thenReturn(new StudentGroup("A2", 1, StudentGroup.Type.BACHELOR));

        IStudentsService studentsService = new StudentsService(studentRepository, studentGroupRepository);

        var student = studentsService.addStudent("Testv1_Name", "310910401RSL19191", 1, "A2");

        Assertions.assertEquals(student.getGroup().getName(), "A2");
        Assertions.assertEquals(student.getName(), "Testv1_Name");
    }

    @Test
    public void Given__StudentsService__When__addStudentIsCalledAndTargetGroupExists__Then__ShouldReturnTheFoundStudentWithTheExistentGroup() throws StudentAdditionException, RepositoryOperationException {

        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);
        var studentRepository = Mockito.mock(StudentRepository.class);


        Mockito.when(studentGroupRepository.getByGroupName("A2"))
                .thenReturn(new StudentGroup("A2", 1, StudentGroup.Type.BACHELOR));

        Mockito.when(studentGroupRepository.createNewGroup("A2"))
                .thenReturn(null);

        IStudentsService studentsService = new StudentsService(studentRepository, studentGroupRepository);

        var student = studentsService.addStudent("Testv1_Name", "310910401RSL19191", 1, "A2");

        Assertions.assertEquals(student.getGroup().getName(), "A2");
        Assertions.assertEquals(student.getName(), "Testv1_Name");
    }

    @Test
    public void Given__StudentsService__When__addStudentIsCalledAndRepositorySaveThrows__Then__ShouldWrapExceptionAndThrowAsWell() throws StudentAdditionException, RepositoryOperationException {

        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);
        var studentRepository = Mockito.mock(StudentRepository.class);

        Mockito.doThrow(new RepositoryOperationException("")).when(studentRepository).save(Mockito.any(Student.class));

        Mockito.when(studentGroupRepository.getByGroupName("A2"))
                .thenReturn(new StudentGroup("A2", 1, StudentGroup.Type.BACHELOR));


        Assertions.assertThrows(StudentAdditionException.class, () -> {
            IStudentsService studentsService = new StudentsService(studentRepository, studentGroupRepository);

            var student = studentsService.addStudent("Testv1_Name", "310910401RSL19191", 1, "A2");
        });
    }

    @Test
    public void Given__StudentsService__When__addStudentIsCalledAndRepositoryCreateNewGroupThrows__Then__ShouldWrapExceptionAndThrowAsWell() throws StudentAdditionException, RepositoryOperationException {

        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);
        var studentRepository = Mockito.mock(StudentRepository.class);

        Mockito.doThrow(new RepositoryOperationException("")).when(studentGroupRepository).createNewGroup(Mockito.any(String.class));

        Mockito.when(studentGroupRepository.getByGroupName("A2"))
                .thenReturn(null);

        Assertions.assertThrows(StudentAdditionException.class, () -> {
            IStudentsService studentsService = new StudentsService(studentRepository, studentGroupRepository);

            studentsService.addStudent("Testv1_Name", "310910401RSL19191", 1, "A2");
        });
    }


//public Student updateStudent(int studentId, String name, int year) throws StudentUpdateException

    @Test
    public void Given__StudentsService__When__updateStudentIsCalledWithValidData__Then__ShouldReturnTheUpdatedStudent() throws StudentUpdateException, RepositoryOperationException, StudentUpdateException {

        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);
        var studentRepository = Mockito.mock(StudentRepository.class);

        var student1 = new Student("student1", 1, "301401RSL191122");

        student1.setId(1);

        Mockito.when(studentRepository.getById(1))
                .thenReturn(student1);

        IStudentsService studentsService = new StudentsService(studentRepository, studentGroupRepository);

        var student = studentsService.updateStudent(1, "Testv1_NameNew", 2);

        Assertions.assertEquals(student.getName(), "Testv1_NameNew");
        Assertions.assertEquals(student.getYear(), 2);
    }

    @Test
    public void Given__StudentsService__When__updateStudentIsCalledWithInexistentStudentId__Then__ShouldThrowStudentUpdateException() throws StudentUpdateException, RepositoryOperationException, StudentUpdateException {

        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);
        var studentRepository = Mockito.mock(StudentRepository.class);


        Mockito.when(studentRepository.getById(1))
                .thenReturn(null);

        IStudentsService studentsService = new StudentsService(studentRepository, studentGroupRepository);

        Assertions.assertThrows(StudentUpdateException.class, () -> {
            studentsService.updateStudent(1, "Testv1_NameNew", 2);
        });
    }

    @Test
    public void Given__StudentsService__When__updateStudentIsCalledAndRepositoryThrows__Then__ShouldWrapExceptionAndThrowAsWell() throws StudentUpdateException, RepositoryOperationException, StudentUpdateException {

        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);
        var studentRepository = Mockito.mock(StudentRepository.class);

        var student1 = new Student("student1", 1, "301401RSL191122");

        student1.setId(1);

        Mockito.when(studentRepository.getById(1))
                .thenReturn(student1);

        Mockito.doThrow(new RepositoryOperationException("Validation fails in repo?"))
                .when(studentRepository).updateStudent(Mockito.any(Student.class));

        IStudentsService studentsService = new StudentsService(studentRepository, studentGroupRepository);

        Assertions.assertThrows(StudentUpdateException.class, () -> {
            studentsService.updateStudent(1, "Testv1_NameNew", 2);
        });
    }
}
