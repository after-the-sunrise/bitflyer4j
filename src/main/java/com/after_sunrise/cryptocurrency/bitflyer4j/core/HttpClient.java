package com.after_sunrise.cryptocurrency.bitflyer4j.core;

import java.util.concurrent.CompletableFuture;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface HttpClient {

    CompletableFuture<String> get(String... paths);

}
