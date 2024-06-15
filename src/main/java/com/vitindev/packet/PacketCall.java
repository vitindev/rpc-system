package com.vitindev.packet;

import java.io.Serializable;
import java.util.UUID;

public record PacketCall<T extends Packet>(
        UUID createdIdPacket,
        UUID replyIdPacket,
        long createdAt,
        String channel,
        T packet
) implements Serializable {
}
