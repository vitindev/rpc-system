package com.vitindev.packet.response;

import com.vitindev.packet.Packet;

public record PacketCalculatorResponse(
        float result
) implements Packet {
}
