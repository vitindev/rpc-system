package com.vitindev;

import com.vitindev.packet.PacketManager;
import com.vitindev.packet.request.PacketAccountRequest;
import com.vitindev.packet.request.PacketCalculatorRequest;
import com.vitindev.packet.response.PacketAccountResponse;
import com.vitindev.packet.response.PacketCalculatorResponse;
import com.vitindev.redis.ConnectionRedisFactory;
import com.vitindev.redis.listener.InterceptorPubSubChannel;
import lombok.Getter;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Main {

    @Getter
    private static final PacketManager packetManager = new PacketManager();
    @Getter
    private static final ConnectionRedisFactory connectionRedisFactory = new ConnectionRedisFactory();

    public static void main(String[] args) {

        final var channel = "Test of Stress";

        connectionRedisFactory.connect();
        connectionRedisFactory.subscribe(new InterceptorPubSubChannel(), channel);

        final var start = System.currentTimeMillis();
        final var atomic2 = new AtomicInteger();

        final int chamada = 500;

        Executors.newFixedThreadPool(4).execute(() -> {

            for (int i = 0; i < chamada; i++) {

                float x = new Random().nextFloat(1000);
                float z = new Random().nextFloat(1000);

                int finalI = i;
                packetManager.call(
                                new PacketCalculatorRequest(x, z, "*"),
                                channel,
                                PacketCalculatorResponse.class).
                        thenAccept(response -> {
                            System.out.println("call-" + finalI + "=" + response.result());
                            atomic2.incrementAndGet();
                        });

            }

        });

        while (atomic2.get() < chamada) {
        }

        System.out.println("Wait time = " + (System.currentTimeMillis() - start));

    }

}