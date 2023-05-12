package org.di;

import com.google.inject.Injector;
import org.GuiceInjectorSingleton;
import org.Main;

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
