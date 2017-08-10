package com.after_sunrise.cryptocurrency.bitflyer4j;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.Environment;
import com.google.inject.Injector;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class TestModule {

    private final Map<Class<?>, Object> mocks = new HashMap<>();

    public TestModule() throws ConfigurationException {
        doAnswer(invocation -> Instant.now()).when(getEnvironment()).getNow();
    }

    public TestModule(Instant now) throws ConfigurationException {
        doAnswer(invocation -> now).when(getEnvironment()).getNow();
    }

    public Environment getEnvironment() {
        return getMock(Environment.class);
    }

    public <T> T getMock(Class<T> clz) {
        return clz.cast(mocks.computeIfAbsent(clz, Mockito::mock));
    }

    public Injector createInjector() {

        Injector injector = mock(Injector.class);

        doAnswer(invocation -> {

            Class<?> c = invocation.getArgumentAt(0, Class.class);

            return getMock(c);

        }).when(injector).getInstance(Mockito.<Class<?>>any());

        return injector;

    }

}
