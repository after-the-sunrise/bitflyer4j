package com.after_sunrise.cryptocurrency.bitflyer4j.service.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.service.OrderService;
import com.google.inject.Inject;
import com.google.inject.Injector;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public class OrderServiceImpl extends BaseService implements OrderService {

    @Inject
    public OrderServiceImpl(Injector injector) {
        super(injector);
    }

}
