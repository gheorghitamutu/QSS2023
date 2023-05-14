package org.services;

import org.Application;
import org.GuiceInjectorSingleton;
import org.application.teachers.ITeachersService;
import org.application.teachers.TeachersService;
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
import org.domain.exceptions.teacher.TeacherAdditionException;
import org.domain.exceptions.teacher.TeacherDeletionFailed;
import org.domain.exceptions.teacher.TeacherNotFoundException;
import org.domain.models.Teacher;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyList;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TeachersServiceTests {
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

    @Test
    public void Given__TeachersService__When__getTeachersWithAtLeastOneExistentTeacher__Then__ReturnTheFoundTeacher() {

        var teacherRepository = Mockito.mock(TeacherRepository.class);

        var teacher1 = new Teacher("teacher1", Teacher.Type.TEACHER);
        var teacher2 = new Teacher("teacher2", Teacher.Type.TEACHER);

        Mockito.when(teacherRepository.readAll()).thenReturn(new ArrayList<>() {
            {
                add(teacher1);
            }

            {
                add(teacher2);
            }
        });

        ITeachersService teachersService = new TeachersService(teacherRepository);

        Assertions.assertEquals(2, teachersService.getTeachers().size());
    }

    @Test
    public void Given__TeachersService__When__deleteTeacherWithInexistentId__Then__ShouldThrow() {

        var teacherRepository = Mockito.mock(TeacherRepository.class);

        Mockito.when(teacherRepository.getById(1)).thenReturn(null);

        ITeachersService teachersService = new TeachersService(teacherRepository);

        Assertions.assertThrows(TeacherNotFoundException.class, () -> teachersService.deleteTeacher(1));
    }

    @Test
    public void Given__TeachersService__When__deleteAllIsCalledAndRepositoryFails__Then__ShouldWrapExceptionAndThrowItFurther() throws RepositoryOperationException {

        var teacherRepository = Mockito.mock(TeacherRepository.class);

        var teacher1 = new Teacher("teacher1", Teacher.Type.TEACHER);
        var teacher2 = new Teacher("teacher2", Teacher.Type.TEACHER);

        Mockito.when(teacherRepository.readAll()).thenReturn(new ArrayList<>() {
            {
                add(teacher1);
            }

            {
                add(teacher2);
            }
        });

        Mockito.doThrow(new RepositoryOperationException("Repo failure")).when(teacherRepository).deleteMany(anyList());

        ITeachersService teachersService = new TeachersService(teacherRepository);

        Assertions.assertThrows(TeacherDeletionFailed.class, teachersService::deleteAll);
    }

    @Test
    public void Given__TeachersService__When__deleteTeachersIsCalledAndRepositoryFails__Then__ShouldWrapExceptionAndThrowItFurther() throws RepositoryOperationException {

        var teacherRepository = Mockito.mock(TeacherRepository.class);

        var teacher1 = new Teacher("teacher1", Teacher.Type.TEACHER);
        var teacher2 = new Teacher("teacher2", Teacher.Type.TEACHER);

        Mockito.when(teacherRepository.readAll()).thenReturn(new ArrayList<>() {
            {
                add(teacher1);
            }

            {
                add(teacher2);
            }
        });

        Mockito.doThrow(new RepositoryOperationException("Repo failure")).when(teacherRepository).deleteMany(anyList());

        ITeachersService teachersService = new TeachersService(teacherRepository);

        Assertions.assertThrows(TeacherDeletionFailed.class, () -> teachersService.deleteTeachers("teacher1"));
    }

    @Test
    public void Given__TeachersService__When__getTeacherByIdIsCalledAndTeachersIsNotFound__Then__ShouldThrow() {

        var teacherRepository = Mockito.mock(TeacherRepository.class);

        var teacher1 = new Teacher("teacher1", Teacher.Type.TEACHER);

        teacher1.setId(1);

        Mockito.when(teacherRepository.getById(1)).thenReturn(null);

        ITeachersService teachersService = new TeachersService(teacherRepository);

        Assertions.assertThrows(TeacherNotFoundException.class, () -> teachersService.getTeacherById(1));
    }

    @Test
    public void Given__TeachersService__When__addTeacherIsCalledAndRepositorySaveThrows__Then__ShouldWrapExceptionAndThrowAsWell() throws RepositoryOperationException {

        var teacherRepository = Mockito.mock(TeacherRepository.class);

        Mockito.doThrow(new RepositoryOperationException("")).when(teacherRepository).save(Mockito.any(Teacher.class));

        Mockito.when(teacherRepository.getByName("teacher1")).thenReturn(new Teacher("teacher1", Teacher.Type.TEACHER));


        Assertions.assertThrows(TeacherAdditionException.class, () -> {
            ITeachersService teachersService = new TeachersService(teacherRepository);

            var teacher = teachersService.addTeacher("teacher1", Teacher.Type.TEACHER);
        });
    }

    @Test
    public void Given__TeachersService__When__addTeacherIsCalledAndTargetTeacherExists__Then__ShouldReturnTheFoundTeacher() throws TeacherAdditionException, RepositoryOperationException {

        var teacherRepository = Mockito.mock(TeacherRepository.class);

        Mockito.when(teacherRepository.getByName("teacher1")).thenReturn(new Teacher("teacher1", Teacher.Type.TEACHER));

        Mockito.when(teacherRepository.createNewTeacher("teacher1", Teacher.Type.COLLABORATOR)).thenReturn(null);

        ITeachersService teachersService = new TeachersService(teacherRepository);

        var teacher = teachersService.addTeacher("teacher1", Teacher.Type.COLLABORATOR);

        Assertions.assertEquals(teacher.getType(), Teacher.Type.TEACHER);
        Assertions.assertEquals(teacher.getName(), "teacher1");
    }
}
