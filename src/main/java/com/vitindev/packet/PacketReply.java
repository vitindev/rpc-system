package com.vitindev.packet;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public record PacketReply<T extends Packet>(
        UUID uuid,
        CompletableFuture<T> future
) {
}
