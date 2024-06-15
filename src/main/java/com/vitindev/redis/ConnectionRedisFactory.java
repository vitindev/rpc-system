package com.vitindev.redis;

import redis.clients.jedis.*;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ConnectionRedisFactory {

    private JedisPool jedisPool;
    private final ExecutorService threadService = Executors.newSingleThreadExecutor();

    public synchronized void connect() {

        final var poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(5);
        poolConfig.setMinIdle(2);
        poolConfig.setMaxIdle(2);

        jedisPool = new JedisPool(poolConfig);

    }

    public void disconnect() {

        if (jedisPool != null)
            jedisPool.close();

        threadService.shutdown();
    }

    public void subscribe(BinaryJedisPubSub jedisPubSub, String channel) {
        threadService.execute(() -> operation(jedis -> jedis.subscribe(jedisPubSub, channel.getBytes(StandardCharsets.UTF_8))));
    }

    public void operation(Consumer<Jedis> consumer) {

        try (final var jedis = jedisPool.getResource()) {
            consumer.accept(jedis);
        } catch (Throwable e) {
            e.printStackTrace();
        }

    }

}
