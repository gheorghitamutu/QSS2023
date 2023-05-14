package org.services;

import org.Application;
import org.GuiceInjectorSingleton;
import org.application.studentgroups.IStudentGroupsService;
import org.application.studentgroups.StudentGroupsService;
import org.dataaccess.studentgroup.StudentGroupRepository;
import org.databaseseed.TimetableEntitiesFactory;
import org.di.TestsDI;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.Timeslot.TimeslotDeletionFailed;
import org.domain.exceptions.discipline.DisciplineDeletionFailed;
import org.domain.exceptions.room.RoomDeletionFailed;
import org.domain.exceptions.session.SessionDeletionFailed;
import org.domain.exceptions.student.StudentDeletionFailed;
import org.domain.exceptions.studentgroup.StudentGroupAdditionException;
import org.domain.exceptions.studentgroup.StudentGroupDeletionFailed;
import org.domain.exceptions.studentgroup.StudentGroupNotFoundException;
import org.domain.exceptions.teacher.TeacherDeletionFailed;
import org.domain.models.StudentGroup;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class StudentGroupsServiceTests {
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
    public void Given__StudentGroupsService__When__getStudentGroupsWithAtLeastOneExistentStudentGroup__Then__ReturnTheFoundStudentGroup() {

        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);

        var studentGroup1 = new StudentGroup("group1", 1, StudentGroup.Type.BACHELOR);
        var studentGroup2 = new StudentGroup("group2", 1, StudentGroup.Type.BACHELOR);

        Mockito.when(studentGroupRepository.readAll()).thenReturn(new ArrayList<>() {
            {
                add(studentGroup1);
            }

            {
                add(studentGroup2);
            }
        });

        IStudentGroupsService studentGroupsService = new StudentGroupsService(studentGroupRepository);

        Assertions.assertEquals(2, studentGroupsService.getStudentGroups().size());
    }

    @Test
    public void Given__StudentGroupsService__When__deleteStudentGroupWithInexistentId__Then__ShouldThrow() {

        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);

        Mockito.when(studentGroupRepository.getById(1)).thenReturn(null);

        IStudentGroupsService studentGroupsService = new StudentGroupsService(studentGroupRepository);

        Assertions.assertThrows(StudentGroupNotFoundException.class, () -> studentGroupsService.deleteStudentGroup(1));
    }

    @Test
    public void Given__StudentGroupsService__When__deleteAllIsCalledAndRepositoryFails__Then__ShouldWrapExceptionAndThrowItFurther() throws RepositoryOperationException {

        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);

        var studentGroup1 = new StudentGroup("group1", 1, StudentGroup.Type.BACHELOR);
        var studentGroup2 = new StudentGroup("group2", 1, StudentGroup.Type.BACHELOR);

        Mockito.when(studentGroupRepository.readAll()).thenReturn(new ArrayList<>() {
            {
                add(studentGroup1);
            }

            {
                add(studentGroup2);
            }
        });

        Mockito.doThrow(new RepositoryOperationException("Repo failure")).when(studentGroupRepository).deleteMany(anyList());

        IStudentGroupsService studentGroupsService = new StudentGroupsService(studentGroupRepository);

        Assertions.assertThrows(StudentGroupDeletionFailed.class, studentGroupsService::deleteAll);
    }

    @Test
    public void Given__StudentGroupsService__When__deleteStudentGroupsIsCalledAndRepositoryFails__Then__ShouldWrapExceptionAndThrowItFurther() throws RepositoryOperationException {

        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);

        var studentGroup1 = new StudentGroup("group1", 1, StudentGroup.Type.BACHELOR);
        var studentGroup2 = new StudentGroup("group2", 1, StudentGroup.Type.BACHELOR);

        Mockito.when(studentGroupRepository.readAll()).thenReturn(new ArrayList<>() {
            {
                add(studentGroup1);
            }

            {
                add(studentGroup2);
            }
        });

        Mockito.doThrow(new RepositoryOperationException("Repo failure")).when(studentGroupRepository).deleteMany(anyList());

        IStudentGroupsService studentGroupsService = new StudentGroupsService(studentGroupRepository);

        Assertions.assertThrows(StudentGroupDeletionFailed.class, () -> studentGroupsService.deleteStudentGroup("group1"));
    }

    @Test
    public void Given__StudentGroupsService__When__getStudentGroupByIdIsCalledAndStudentGroupIsNotFound__Then__ShouldThrow() {

        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);

        var studentGroup1 = new StudentGroup("group1", 1, StudentGroup.Type.BACHELOR);

        studentGroup1.setId(1);

        Mockito.when(studentGroupRepository.getById(1)).thenReturn(null);

        IStudentGroupsService studentGroupsService = new StudentGroupsService(studentGroupRepository);

        Assertions.assertThrows(StudentGroupNotFoundException.class, () -> studentGroupsService.getStudentGroupById(1));
    }

    @Test
    public void Given__StudentGroupsService__When__getStudentGroupsByYearIsCalledAndStudentGroupIsFound__Then__ShouldReturnTheFoundStudentGroup() {

        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);

        var studentGroup1 = new StudentGroup("group1", 1, StudentGroup.Type.BACHELOR);
        var studentGroup2 = new StudentGroup("group2", 2, StudentGroup.Type.BACHELOR);

        Mockito.when(studentGroupRepository.readAll()).thenReturn(new ArrayList<>() {
            {
                add(studentGroup1);
            }

            {
                add(studentGroup2);
            }
        });

        IStudentGroupsService studentGroupsService = new StudentGroupsService(studentGroupRepository);
        Assertions.assertEquals(List.of(studentGroup1), studentGroupsService.getStudentGroupsByYear(1));
    }

    @Test
    public void Given__StudentGroupsService__When__getStudentGroupsByTypeIsCalledAndStudentGroupIsFound__Then__ShouldReturnTheFoundStudentGroup() {

        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);

        var studentGroup1 = new StudentGroup("group1", 1, StudentGroup.Type.BACHELOR);
        var studentGroup2 = new StudentGroup("group2", 1, StudentGroup.Type.MASTER);

        Mockito.when(studentGroupRepository.readAll()).thenReturn(new ArrayList<>() {
            {
                add(studentGroup1);
            }

            {
                add(studentGroup2);
            }
        });

        IStudentGroupsService studentGroupsService = new StudentGroupsService(studentGroupRepository);
        Assertions.assertEquals(List.of(studentGroup1), studentGroupsService.getStudentGroupsByType(StudentGroup.Type.BACHELOR));
    }

    @Test
    public void Given__StudentGroupsService__When__addStudentGroupIsCalledAndRepositorySaveThrows__Then__ShouldWrapExceptionAndThrowAsWell() throws RepositoryOperationException {

        var studentGroupRepository = Mockito.mock(StudentGroupRepository.class);

        Mockito.doThrow(new RepositoryOperationException("")).when(studentGroupRepository).save(Mockito.any(StudentGroup.class));

        Mockito.when(studentGroupRepository.getByGroupName("group1")).thenReturn(new StudentGroup("group1", 1, StudentGroup.Type.BACHELOR));


        Assertions.assertThrows(StudentGroupAdditionException.class, () -> {
            IStudentGroupsService studentGroupsService = new StudentGroupsService(studentGroupRepository);

            var studentGroup = studentGroupsService.addStudentGroup("group1", 1, StudentGroup.Type.BACHELOR);
        });
    }
}
