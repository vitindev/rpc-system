package com.vitindev.redis.listener;

import com.vitindev.Main;
import redis.clients.jedis.BinaryJedisPubSub;

import java.nio.charset.StandardCharsets;

public class InterceptorPubSubChannel extends BinaryJedisPubSub {

    @Override
    public void onMessage(byte[] channel, byte[] message) {

        try {
            Main.getPacketManager().listen(message, new String(channel, StandardCharsets.UTF_8));
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

}
