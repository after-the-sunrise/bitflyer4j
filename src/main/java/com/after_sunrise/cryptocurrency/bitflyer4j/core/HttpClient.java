package com.after_sunrise.cryptocurrency.bitflyer4j.core;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;
import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface HttpClient {

    class HttpRequest {

        private final PathType type;

        private final Map<String, String> parameters;

        private final String body;

        public HttpRequest(PathType path) {
            this(path, null, null);
        }

        public HttpRequest(PathType path, Map<String, String> parameters) {
            this(path, parameters, null);
        }

        public HttpRequest(PathType path, String body) {
            this(path, null, body);
        }

        public HttpRequest(PathType type, Map<String, String> parameters, String body) {
            this.type = type;
            this.parameters = parameters;
            this.body = body;
        }

        @Override
        public String toString() {
            return reflectionToString(this, SHORT_PREFIX_STYLE);
        }

        public PathType getType() {
            return type;
        }

        public Map<String, String> getParameters() {
            return parameters;
        }

        public String getBody() {
            return body;
        }

    }

    interface HttpResponse {

        int getCode();

        String getMessage();

        String getBody();

    }

    CompletableFuture<HttpResponse> request(HttpRequest request);

}
