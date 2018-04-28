package com.after_sunrise.cryptocurrency.bitflyer4j.core.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.Environment;
import com.google.inject.Inject;
import com.google.inject.Provider;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.engineio.client.transports.WebSocket;
import lombok.extern.slf4j.Slf4j;

import java.net.URISyntaxException;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
@Slf4j
public class SocketProvider implements Provider<Socket> {

    private final Environment environment;

    @Inject
    public SocketProvider(Environment environment) {
        this.environment = environment;
    }

    @Override
    public Socket get() {

        String endpoint = environment.getSocketEndpoint();

        Socket socket;

        try {

            IO.Options options = new IO.Options();

            options.transports = new String[]{WebSocket.NAME};

            options.reconnection = true;

            socket = IO.socket(endpoint, options);

        } catch (URISyntaxException e) {

            throw new RuntimeException(endpoint, e);

        }

        log.debug("Created instance : {}", endpoint);

        return socket;

    }

}
