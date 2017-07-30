package com.after_sunrise.cryptocurrency.bitflyer4j.core;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface HttpClient {

    @Value
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    class HttpRequest {

        private final PathType type;

        private final Map<String, String> parameters;

        private final String body;

    }

    interface HttpResponse {

        int getCode();

        String getMessage();

        String getBody();

    }

    CompletableFuture<HttpResponse> request(HttpRequest request);

}
