package org.application;


import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import org.application.application.disciplines.DisciplinesService;
import org.application.application.disciplines.IDisciplinesService;
import org.application.application.rooms.IRoomsService;
import org.application.application.rooms.RoomsService;
import org.application.application.sessions.ISessionsService;
import org.application.application.sessions.SessionsService;
import org.application.application.studentgroups.IStudentGroupsService;
import org.application.application.studentgroups.StudentGroupsService;
import org.application.application.students.IStudentsService;
import org.application.application.students.StudentsService;
import org.application.application.teachers.ITeachersService;
import org.application.application.teachers.TeachersService;
import org.application.application.timeslots.ITimeslotsService;
import org.application.application.timeslots.TimeslotsService;
import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.database.MainDatabaseHibernateProvider;
import org.application.dataaccess.database.TestsDatabaseHibernateProvider;
import org.application.dataaccess.discipline.DisciplineRepository;
import org.application.dataaccess.discipline.IDisciplineRepository;
import org.application.dataaccess.room.IRoomRepository;
import org.application.dataaccess.room.RoomRepository;
import org.application.dataaccess.session.ISessionRepository;
import org.application.dataaccess.session.SessionRepository;
import org.application.dataaccess.student.IStudentRepository;
import org.application.dataaccess.student.StudentRepository;
import org.application.dataaccess.studentgroup.IStudentGroupRepository;
import org.application.dataaccess.studentgroup.StudentGroupRepository;
import org.application.dataaccess.teacher.ITeacherRepository;
import org.application.dataaccess.teacher.TeacherRepository;
import org.application.dataaccess.timeslot.ITimeslotRepository;
import org.application.dataaccess.timeslot.TimeslotRepository;

public class Main {

    public static void main(String[] args) {

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

