package com.after_sunrise.cryptocurrency.bitflyer4j.service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface AccountService {

    CompletableFuture<List<String>> getPermissions();

}
