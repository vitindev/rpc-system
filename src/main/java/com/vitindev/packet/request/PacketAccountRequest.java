package com.vitindev.packet.request;

import com.vitindev.packet.Packet;

import java.util.UUID;

public record PacketAccountRequest(
        long id,
        UUID uuid,
        String name
) implements Packet {
}
