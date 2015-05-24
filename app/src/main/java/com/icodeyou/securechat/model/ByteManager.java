package com.icodeyou.securechat.model;

import java.io.Serializable;


public class ByteManager implements Serializable {

	byte[] bytes;

    public ByteManager(byte[] bytes) {
        this.bytes = bytes;
    }

    public ByteManager() {
    }

    public byte[] getBytes() {
        return bytes;
    }
}
