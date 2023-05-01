package org.application.di;

import com.google.inject.Injector;
import org.application.GuiceInjectorSingleton;
import org.application.Main;

public class TestsDI {

    public static boolean DiInitialized = false;

    public static void initializeDi() {
        if (!DiInitialized) {

            Injector appInjector = Main.setupDependenciesInjector(true);
            GuiceInjectorSingleton.INSTANCE.setInjector(appInjector);

            DiInitialized = true;
        }
    }
}
