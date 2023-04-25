package org.application;



import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Provides;
import org.application.dataaccess.IHibernateProvider;
import org.application.dataaccess.IStudentsRepository;
import org.application.dataaccess.MainDatabaseHibernateProvider;
import org.application.dataaccess.StudentsRepository;

public class Main {
    public static void main(String[] args) {
        Application app =
                Guice.createInjector(
                                new MessageModule(),
                                new AbstractModule() {
                                    @Override
                                    protected void configure() {
                                        bind(IHibernateProvider.class).toInstance(new MainDatabaseHibernateProvider());
                                        bind(IStudentsRepository.class).to(StudentsRepository.class);
                                    }
                                })
                        .getInstance(Application.class);

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

