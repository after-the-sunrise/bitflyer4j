package com.after_sunrise.cryptocurrency.bitflyer4j.core;

import com.pubnub.api.PubNub;

import java.io.Closeable;

/**
 * @author takanori.takase
 * @version 0.0.1
 */
public interface PubNubFactory extends Closeable {

    PubNub get();

}
