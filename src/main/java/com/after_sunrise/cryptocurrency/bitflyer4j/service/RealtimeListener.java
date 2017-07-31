package com.after_sunrise.cryptocurrency.bitflyer4j.service;

import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Board;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Execution;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Tick;

import java.util.List;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface RealtimeListener {

    void onBoards(List<Board> values);

    void onTicks(List<Tick> values);

    void onExecutions(List<Execution> values);

}
