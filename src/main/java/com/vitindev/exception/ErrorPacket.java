package com.vitindev.exception;

import com.vitindev.packet.Packet;

public interface ErrorPacket extends Packet {

    Exception createException();

}
