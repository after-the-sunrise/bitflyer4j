package com.after_sunrise.cryptocurrency.bitflyer4j.service;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Board;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Product;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Tick;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface MarketService {

    CompletableFuture<List<Product>> getProducts();

    CompletableFuture<Board> getBoard(String product);

    CompletableFuture<Tick> getTick(String product);

}
