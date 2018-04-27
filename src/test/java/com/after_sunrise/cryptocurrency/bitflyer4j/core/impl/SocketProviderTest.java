package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.Environment;
import io.socket.client.Socket;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SocketProviderTest {

    private SocketProvider target;

    private Environment environment;

    @BeforeMethod
    public void setUp() {

        environment = mock(Environment.class);

        target = new SocketProvider(environment);

    }

    @Test
    public void testGet() {

        when(environment.getSocketEndpoint()).thenReturn("https://localhost/");

        Socket socket = target.get();

        socket.close();

    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testGet_Exception() {

        when(environment.getSocketEndpoint()).thenReturn("https://local host/");

        Socket socket = target.get();

        socket.close();

    }

}
