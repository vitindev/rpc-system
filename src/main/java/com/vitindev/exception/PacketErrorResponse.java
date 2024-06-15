package com.vitindev.exception;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class PacketErrorResponse implements ErrorPacket {

    private final Exception exception;

    @Override
    public Exception createException() {
        return exception;
    }

}
