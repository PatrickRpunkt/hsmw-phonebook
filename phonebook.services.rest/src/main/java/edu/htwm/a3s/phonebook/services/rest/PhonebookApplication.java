package edu.htwm.a3s.phonebook.services.rest;


import com.sun.jersey.spi.inject.SingletonTypeInjectableProvider;
import edu.htwm.a3s.phonebook.services.core.PhonebookService;
import edu.htwm.a3s.phonebook.services.core.impl.PhonebookServiceInMemory;
import edu.htwm.a3s.phonebook.services.rest.impl.PhonebookResourceImpl;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * An {@link javax.ws.rs.core.Application }
 *
 * @author std /mbenndor
 *
 */
public class PhonebookApplication extends Application {

    private final Set<Object> singletons = new HashSet<Object>();
    private final Set<Class<?>> resourcesToRegister = new HashSet<Class<?>>();
    private final PhonebookService phonebookService;

    public PhonebookApplication() {
        // add the phonebook service
        phonebookService = new PhonebookServiceInMemory();
        // make the phonebook service accessable for Context and create the phonebookServiceProvider
        SingletonTypeInjectableProvider<Context, PhonebookService> phonebookServiceProvider = new SingletonTypeInjectableProvider<Context, PhonebookService>(
                PhonebookService.class, phonebookService) {
        };
        getSingletons().add(phonebookServiceProvider);
        // register the resource
        getClasses().add(PhonebookResourceImpl.class);
    }

    @Override
    public final Set<Class<?>> getClasses() {
        return resourcesToRegister;
    }

    @Override
    public final Set<Object> getSingletons() {
        return singletons;
    }
}
