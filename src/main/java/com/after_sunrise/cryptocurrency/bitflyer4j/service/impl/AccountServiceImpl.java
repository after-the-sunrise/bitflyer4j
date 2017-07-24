package com.after_sunrise.cryptocurrency.bitflyer4j.service.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.HttpClient;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.PathType;
import com.after_sunrise.cryptocurrency.bitflyer4j.service.AccountService;
import com.google.inject.Inject;
import com.google.inject.Injector;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class AccountServiceImpl extends BaseService implements AccountService {

    @Inject
    public AccountServiceImpl(Injector injector) {
        super(injector);
    }

    @Override
    public CompletableFuture<List<String>> getPermissions() {

        HttpClient.HttpRequest req = new HttpClient.HttpRequest(PathType.PERMISSION);

        CompletableFuture<HttpClient.HttpResponse> future = client.request(req);

        return future.thenApply(s -> gson.fromJson(s.getBody(), TYPE_STRINGS));

    }

}
