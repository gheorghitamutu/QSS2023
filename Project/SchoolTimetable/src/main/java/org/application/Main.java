package org.application;


import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Provides;
import org.application.application.students.IStudentsService;
import org.application.application.students.StudentsService;
import org.application.dataaccess.database.IHibernateProvider;
import org.application.dataaccess.discipline.DisciplineRepository;
import org.application.dataaccess.discipline.IDisciplineRepository;
import org.application.dataaccess.room.IRoomRepository;
import org.application.dataaccess.room.RoomRepository;
import org.application.dataaccess.session.ISessionRepository;
import org.application.dataaccess.session.SessionRepository;
import org.application.dataaccess.student.IStudentRepository;
import org.application.dataaccess.database.MainDatabaseHibernateProvider;
import org.application.dataaccess.student.StudentRepository;
import org.application.dataaccess.studentgroup.IStudentGroupRepository;
import org.application.dataaccess.studentgroup.StudentGroupRepository;

public class Main {
    public static void main(String[] args) {
        Injector appInjector = Guice.createInjector(new MessageModule(), new AbstractModule() {
            @Override
            protected void configure() {
                bind(IHibernateProvider.class).toInstance(new MainDatabaseHibernateProvider());

                bind(IStudentRepository.class).to(StudentRepository.class);
                bind(IStudentGroupRepository.class).to(StudentGroupRepository.class);
                bind(ISessionRepository.class).to(SessionRepository.class);
                bind(IStudentGroupRepository.class).to(StudentGroupRepository.class);
                bind(IDisciplineRepository.class).to(DisciplineRepository.class);
                bind(IRoomRepository.class).to(RoomRepository.class);

                bind(IStudentsService.class).to(StudentsService.class);

            }
        });

        var app = appInjector.getInstance(Application.class);

        GuiceInjectorSingleton.INSTANCE.setInjector(appInjector);

        app.run();
    }

    static class MessageModule extends AbstractModule {
        @Provides
        String provideMessage() {
            return "Hello, Guice!";
        }

        @Override
        protected void configure() {

        }
    }
}

