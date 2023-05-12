package org;

import com.google.inject.Injector;

public enum GuiceInjectorSingleton {
    INSTANCE;
    private Injector injector;
    public Injector getInjector() {
        return injector;
    }
    public void setInjector(Injector injector) {
        this.injector = injector;
    }
}
