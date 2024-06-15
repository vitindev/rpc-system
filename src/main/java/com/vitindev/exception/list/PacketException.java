package com.vitindev.exception.list;

public class PacketException extends RuntimeException {

    public PacketException(String message) {
        super(message);
    }

    public PacketException(Throwable cause) {
        super(cause);
    }

}
