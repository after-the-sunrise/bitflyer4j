package com.after_sunrise.cryptocurrency.bitflyer4j;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.Environment;
import com.google.inject.Injector;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class TestModule {

    private final Map<Class<?>, Object> mocks = new HashMap<>();

    private final Environment environment;

    private long timestamp = System.currentTimeMillis();

    public TestModule() throws ConfigurationException {

        environment = mock(Environment.class);

        doAnswer(invocation -> timestamp).when(environment).getTimeMillis();

        mocks.put(Environment.class, environment);

    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public <T> T getMock(Class<T> clz) {
        return clz.cast(mocks.computeIfAbsent(clz, Mockito::mock));
    }

    public void setProperty(String key, String value) {

        Configuration conf = getMock(Configuration.class);

        when(conf.getString(key)).thenReturn(value);

        when(conf.getString(eq(key), anyString())).thenReturn(value);

    }

    public Injector createInjector() {

        Injector injector = mock(Injector.class);

        doAnswer(invocation -> getMock(invocation.getArgumentAt(0, Class.class))) //
                .when(injector).getInstance(any(Class.class));

        return injector;

    }

}
