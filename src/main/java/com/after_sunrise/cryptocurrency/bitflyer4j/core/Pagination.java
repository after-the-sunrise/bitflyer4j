package com.after_sunrise.cryptocurrency.bitflyer4j.core;

import com.google.gson.annotations.SerializedName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
@Value
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Pagination {

    @SerializedName("count")
    private final Long count;

    @SerializedName("before")
    private final Long before;

    @SerializedName("after")
    private final Long after;

}
