package com.vitindev.manager;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.vitindev.Main;
import lombok.Getter;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Getter
public class RpcManager {

    private JedisPool jedisPool;

    private final Map<UUID, RpcResponse<?>> pendingCalls;

    public RpcManager() {
        pendingCalls = new ConcurrentHashMap<>();
    }

    public <TResponse> CompletableFuture<TResponse> call(TResponse t, UUID uuid) {

        RpcResponse<TResponse> response = new RpcResponse<>(t);
        pendingCalls.put(uuid, response);

        /*
         * PUBLISH IN CHANNEL
         */

        JsonObject object = new JsonObject();
        object.addProperty("uuid", uuid.toString());
        object.addProperty("call", t.getClass().getSimpleName());

        operation(jedis -> jedis.publish(Channels.RPC_SERVER_SIDE.name(), Main.getGson().toJson(object)));
        return response.getCompletableFuture();
    }

    public void connect() {

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(10);
        poolConfig.setMinIdle(2);
        poolConfig.setMaxIdle(2);

        jedisPool = new JedisPool(poolConfig);

    }

    public void disconnect() {

        if (jedisPool != null)
            jedisPool.close();

    }

    public void subscribe(JedisPubSub jedisPubSub, String channel) {
        Executors.newSingleThreadExecutor().execute(() -> operation(jedis -> jedis.subscribe(jedisPubSub, channel)));
    }

    public void operation(Consumer<Jedis> consumer) {

        try (Jedis jedis = jedisPool.getResource()) {
            consumer.accept(jedis);
        }

    }

}
