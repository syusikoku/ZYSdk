package com.zysdk.vulture.clib.utils;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

public class SerializableMap implements Serializable {
    private ConcurrentHashMap map;

    public ConcurrentHashMap getMap() {
        return map;
    }

    public void setMap(ConcurrentHashMap map) {
        this.map = map;
    }
}
