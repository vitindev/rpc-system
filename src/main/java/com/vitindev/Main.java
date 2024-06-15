package com.vitindev;

import com.vitindev.packet.PacketManager;
import com.vitindev.packet.request.PacketCalculatorRequest;
import com.vitindev.packet.response.PacketCalculatorResponse;
import com.vitindev.redis.ConnectionRedisFactory;
import com.vitindev.redis.listener.InterceptorPubSubChannel;
import lombok.Getter;

import java.util.Random;

public class Main {

    @Getter
    private static final PacketManager packetManager = new PacketManager();
    @Getter
    private static final ConnectionRedisFactory connectionRedisFactory = new ConnectionRedisFactory();

    public static void main(String[] args) {

        final var channel = "Test";

        connectionRedisFactory.connect();
        connectionRedisFactory.subscribe(new InterceptorPubSubChannel(), channel);

        packetManager.call(
                        new PacketCalculatorRequest(
                                new Random().nextFloat(1000),
                                new Random().nextFloat(1000),
                                "*"),
                        channel,
                        PacketCalculatorResponse.class).
                thenAccept(response -> System.out.println(response.result()));

    }

}