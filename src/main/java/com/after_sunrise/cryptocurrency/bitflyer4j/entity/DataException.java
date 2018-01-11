package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import lombok.Getter;

/**
 * @author takanori.takase
 * @version 0.3.2
 */
@Getter
public class DataException extends RuntimeException {

    private final int code;

    private final String text;

    private final String data;

    public DataException(int code, String text, String data, Throwable cause) {

        super(code + " " + text, cause);

        this.code = code;

        this.text = text;

        this.data = data;

    }

}
