package org;

import com.google.inject.Injector;

/**
 * The GuiceInjectorSingleton class represents a singleton for the Guice injector.
 * It is used to inject dependencies into classes that are not managed by Guice.
 */
public enum GuiceInjectorSingleton {

    /**
     * The GuiceInjectorSingleton instance.
     */
    INSTANCE;

    /**
     * The Guice injector.
     */
    private Injector injector;

    /**
     * Returns the Guice injector.
     * @return The Guice injector.
     */
    public Injector getInjector() {
        return injector;
    }

    /**
     * Sets the Guice injector.
     * @param injector The Guice injector.
     */
    public void setInjector(Injector injector) {
        this.injector = injector;
    }
}
