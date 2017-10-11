package com.after_sunrise.cryptocurrency.bitflyer4j.core;

import java.util.concurrent.ExecutorService;

/**
 * @author takanori.takase
 * @version 0.0.1
 **/
public interface ExecutorFactory {

    ExecutorService get(Class<?> clazz);

    ExecutorService get(Class<?> clazz, int threads);

    void shutdown();

}
