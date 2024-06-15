package com.vitindev.packet.request;

import com.vitindev.packet.Packet;

public record PacketCalculatorRequest(
        float x,
        float z,
        String operation
) implements Packet {
}
