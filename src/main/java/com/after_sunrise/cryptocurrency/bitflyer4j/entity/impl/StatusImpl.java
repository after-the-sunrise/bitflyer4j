package com.after_sunrise.cryptocurrency.bitflyer4j.entity.impl;

import com.after_sunrise.cryptocurrency.bitflyer4j.core.StatusType;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Entity;
import com.after_sunrise.cryptocurrency.bitflyer4j.entity.Status;
import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
@Getter
@AllArgsConstructor
public class StatusImpl extends Entity implements Status {

    @SerializedName("status")
    private final StatusType status;

}
