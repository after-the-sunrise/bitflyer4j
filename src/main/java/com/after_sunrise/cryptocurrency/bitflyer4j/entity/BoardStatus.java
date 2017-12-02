package com.after_sunrise.cryptocurrency.bitflyer4j.entity;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.BoardStatusType;
import com.after_sunrise.cryptocurrency.bitflyer4j.core.StatusType;
import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface BoardStatus {

    StatusType getHealth();

    BoardStatusType getState();

    Map<String, ?> getData();

    @Getter
    @Builder
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    class Request extends Entity {

        @SerializedName("product_code")
        private final String product;

    }

}
