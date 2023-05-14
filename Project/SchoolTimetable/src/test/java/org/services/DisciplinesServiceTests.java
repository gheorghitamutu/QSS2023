package org.services;

import org.Application;
import org.GuiceInjectorSingleton;
import org.application.disciplines.DisciplinesService;
import org.application.disciplines.IDisciplinesService;
import org.dataaccess.discipline.DisciplineRepository;
import org.dataaccess.teacher.TeacherRepository;
import org.databaseseed.TimetableEntitiesFactory;
import org.di.TestsDI;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.Timeslot.TimeslotDeletionFailed;
import org.domain.exceptions.discipline.DisciplineAdditionException;
import org.domain.exceptions.discipline.DisciplineDeletionFailed;
import org.domain.exceptions.discipline.DisciplineNotFoundException;
import org.domain.exceptions.room.RoomDeletionFailed;
import org.domain.exceptions.session.SessionDeletionFailed;
import org.domain.exceptions.student.StudentDeletionFailed;
import org.domain.exceptions.studentgroup.StudentGroupDeletionFailed;
import org.domain.exceptions.teacher.TeacherDeletionFailed;
import org.domain.exceptions.teacher.TeacherNotFoundException;
import org.domain.models.Discipline;
import org.domain.models.Teacher;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.mockito.ArgumentMatchers.anyList;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DisciplinesServiceTests {
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
    public void Given__DisciplinesService__When__getDisciplinesWithAtLeastOneExistentDiscipline__Then__ReturnTheFoundDisciplines() {

        var disciplineRepository = Mockito.mock(DisciplineRepository.class);
        var teacherRepository = Mockito.mock(TeacherRepository.class);

        var discipline1 = new Discipline("discipline1", 4);
        var discipline2 = new Discipline("discipline2", 4);

        Mockito.when(disciplineRepository.readAll()).thenReturn(new ArrayList<>() {
            {
                add(discipline1);
            }

            {
                add(discipline2);
            }
        });

        IDisciplinesService disciplinesService = new DisciplinesService(disciplineRepository, teacherRepository);

        Assertions.assertEquals(2, disciplinesService.getDisciplines().size());
    }

    @Test
    public void Given__DisciplinesService__When__deleteDisciplineWithInexistentId__Then__ShouldThrow() {

        var disciplineRepository = Mockito.mock(DisciplineRepository.class);
        var teacherRepository = Mockito.mock(TeacherRepository.class);

        Mockito.when(disciplineRepository.getById(1)).thenReturn(null);

        IDisciplinesService disciplinesService = new DisciplinesService(disciplineRepository, teacherRepository);

        Assertions.assertThrows(DisciplineNotFoundException.class, () -> disciplinesService.deleteDiscipline(1));
    }

    @Test
    public void Given__DisciplinesService__When__deleteAllIsCalledAndRepositoryFails__Then__ShouldWrapExceptionAndThrowItFurther() throws RepositoryOperationException {

        var disciplineRepository = Mockito.mock(DisciplineRepository.class);
        var teacherRepository = Mockito.mock(TeacherRepository.class);

        var discipline1 = new Discipline("discipline1", 4);
        var discipline2 = new Discipline("discipline2", 4);

        Mockito.when(disciplineRepository.readAll()).thenReturn(new ArrayList<>() {
            {
                add(discipline1);
            }

            {
                add(discipline2);
            }
        });

        Mockito.doThrow(new RepositoryOperationException("Repo failure")).when(disciplineRepository).deleteMany(anyList());

        IDisciplinesService disciplinesService = new DisciplinesService(disciplineRepository, teacherRepository);

        Assertions.assertThrows(DisciplineDeletionFailed.class, disciplinesService::deleteAll);
    }

    @Test
    public void Given__DisciplinesService__When__deleteDisciplinesIsCalledAndRepositoryFails__Then__ShouldWrapExceptionAndThrowItFurther() throws RepositoryOperationException {

        var disciplineRepository = Mockito.mock(DisciplineRepository.class);
        var teacherRepository = Mockito.mock(TeacherRepository.class);

        var discipline1 = new Discipline("discipline1", 4);
        var discipline2 = new Discipline("discipline2", 4);

        Mockito.when(disciplineRepository.readAll()).thenReturn(new ArrayList<>() {
            {
                add(discipline1);
            }

            {
                add(discipline2);
            }
        });

        Mockito.doThrow(new RepositoryOperationException("Repo failure")).when(disciplineRepository).deleteMany(anyList());

        IDisciplinesService disciplinesService = new DisciplinesService(disciplineRepository, teacherRepository);

        Assertions.assertThrows(DisciplineDeletionFailed.class, () -> disciplinesService.deleteDisciplines("discipline1"));
    }

    @Test
    public void Given__DisciplinesService__When__getDisciplineByIdIsCalledAndDisciplineIsNotFound__Then__ShouldThrow() {

        var disciplineRepository = Mockito.mock(DisciplineRepository.class);
        var teacherRepository = Mockito.mock(TeacherRepository.class);

        var discipline1 = new Discipline("discipline1", 4);

        discipline1.setId(1);

        Mockito.when(disciplineRepository.getById(1)).thenReturn(null);

        IDisciplinesService disciplinesService = new DisciplinesService(disciplineRepository, teacherRepository);

        Assertions.assertThrows(DisciplineNotFoundException.class, () -> disciplinesService.getDisciplineById(1));
    }

    @Test
    public void Given__DisciplinesService__When__addDisciplineIsCalledAndRepositorySaveThrows__Then__ShouldWrapExceptionAndThrowAsWell() throws RepositoryOperationException {

        var disciplineRepository = Mockito.mock(DisciplineRepository.class);
        var teacherRepository = Mockito.mock(TeacherRepository.class);

        Mockito.doThrow(new RepositoryOperationException("")).when(disciplineRepository).save(Mockito.any(Discipline.class));

        Mockito.when(disciplineRepository.getByName("discipline1")).thenReturn(new Discipline("discipline1", 4));


        Assertions.assertThrows(DisciplineAdditionException.class, () -> {
            IDisciplinesService disciplinesService = new DisciplinesService(disciplineRepository, teacherRepository);

            var discipline = disciplinesService.addDiscipline("discipline1", 4);
        });
    }

    @Test
    public void Given__DisciplinesService__When__addDisciplineIsCalledAndTargetDisciplineExists__Then__ShouldReturnTheFoundDiscipline() throws DisciplineAdditionException, RepositoryOperationException {

        var disciplineRepository = Mockito.mock(DisciplineRepository.class);
        var teacherRepository = Mockito.mock(TeacherRepository.class);


        Mockito.when(disciplineRepository.getByName("discipline1")).thenReturn(new Discipline("discipline1", 4));

        Mockito.when(disciplineRepository.createNewDiscipline("discipline1", 5)).thenReturn(null);

        IDisciplinesService disciplinesService = new DisciplinesService(disciplineRepository, teacherRepository);

        var discipline = disciplinesService.addDiscipline("discipline1", 5);

        Assertions.assertEquals(discipline.getCredits(), 4);
        Assertions.assertEquals(discipline.getName(), "discipline1");
    }

    @Test
    public void Given__DisciplinesService__When__addTeacherToDisciplineIsCalledAndTeacherIsNotFound__Then__ShouldThrow() {
        var disciplineRepository = Mockito.mock(DisciplineRepository.class);
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

        IDisciplinesService disciplinesService = new DisciplinesService(disciplineRepository, teacherRepository);

        Assertions.assertThrows(TeacherNotFoundException.class, () -> disciplinesService.addTeacherToDiscipline("teacher3", "discipline1"));
    }
}