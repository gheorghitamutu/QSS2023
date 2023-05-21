package org;


import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import org.application.disciplines.DisciplinesService;
import org.application.disciplines.IDisciplinesService;
import org.application.rooms.IRoomsService;
import org.application.rooms.RoomsService;
import org.application.sessions.ISessionsService;
import org.application.sessions.SessionsService;
import org.application.studentgroups.IStudentGroupsService;
import org.application.studentgroups.StudentGroupsService;
import org.application.students.IStudentsService;
import org.application.students.StudentsService;
import org.application.teachers.ITeachersService;
import org.application.teachers.TeachersService;
import org.application.timeslots.ITimeslotsService;
import org.application.timeslots.TimeslotsService;
import org.dataaccess.database.IHibernateProvider;
import org.dataaccess.database.MainDatabaseHibernateProvider;
import org.dataaccess.database.TestsDatabaseHibernateProvider;
import org.dataaccess.discipline.DisciplineRepository;
import org.dataaccess.discipline.IDisciplineRepository;
import org.dataaccess.room.IRoomRepository;
import org.dataaccess.room.RoomRepository;
import org.dataaccess.session.ISessionRepository;
import org.dataaccess.session.SessionRepository;
import org.dataaccess.student.IStudentRepository;
import org.dataaccess.student.StudentRepository;
import org.dataaccess.studentgroup.IStudentGroupRepository;
import org.dataaccess.studentgroup.StudentGroupRepository;
import org.dataaccess.teacher.ITeacherRepository;
import org.dataaccess.teacher.TeacherRepository;
import org.dataaccess.timeslot.ITimeslotRepository;
import org.dataaccess.timeslot.TimeslotRepository;
import org.domain.exceptions.RepositoryOperationException;
import org.domain.exceptions.validations.ValidationException;

public class Main {

    public static void main(String[] args) throws RepositoryOperationException, ValidationException {

        var appInjector = setupDependenciesInjector(false);
        var app = appInjector.getInstance(Application.class);
        GuiceInjectorSingleton.INSTANCE.setInjector(appInjector);

        app.run();
    }

    public static Injector setupDependenciesInjector(boolean testMode) {

        return Guice.createInjector(new MessageModule(), new AbstractModule() {
            @Override
            protected void configure() {

                if (testMode) {
                    bind(IHibernateProvider.class).toInstance(new TestsDatabaseHibernateProvider());
                } else {
                    bind(IHibernateProvider.class).toInstance(new MainDatabaseHibernateProvider());
                }

                bind(IDisciplineRepository.class).to(DisciplineRepository.class);
                bind(IRoomRepository.class).to(RoomRepository.class);
                bind(ISessionRepository.class).to(SessionRepository.class);
                bind(IStudentGroupRepository.class).to(StudentGroupRepository.class);
                bind(IStudentRepository.class).to(StudentRepository.class);
                bind(ITeacherRepository.class).to(TeacherRepository.class);
                bind(ITimeslotRepository.class).to(TimeslotRepository.class);

                bind(IDisciplinesService.class).to(DisciplinesService.class);
                bind(IRoomsService.class).to(RoomsService.class);
                bind(ISessionsService.class).to(SessionsService.class);
                bind(IStudentGroupsService.class).to(StudentGroupsService.class);
                bind(IStudentsService.class).to(StudentsService.class);
                bind(ITeachersService.class).to(TeachersService.class);
                bind(ITimeslotsService.class).to(TimeslotsService.class);
            }
        });
    }

    public static class MessageModule extends AbstractModule {
        @Provides
        String provideMessage() {
            return "Hello, Guice!";
        }

        @Override
        protected void configure() {
        }
    }
}

