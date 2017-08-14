package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.unmodifiableMap;

/**
 * @author takanori.takase
 * @version 0.0.6
 */
@Getter
public class RejectException extends RuntimeException {

    private final int code;

    private final String text;

    private final Map<String, String> details;

    public RejectException(int code, String text, Map<String, String> details) {

        super(code + " " + text);

        this.code = code;

        this.text = text;

        this.details = unmodifiableMap(new HashMap<>(details));

    }

}
