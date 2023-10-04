package com.vitindev.client;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vitindev.Main;
import com.vitindev.manager.Channels;
import com.vitindev.manager.RpcResponse;
import redis.clients.jedis.JedisPubSub;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class PubSubClient extends JedisPubSub {

    private final RpcClient rpcClient;

    public PubSubClient(RpcClient rpcClient) {
        this.rpcClient = rpcClient;
    }

    @Override
    public void onMessage(String channel, String message) {

        if (!channel.equalsIgnoreCase(Channels.RPC_CLIENT_SIDE.name()))
            return;

        JsonObject object = JsonParser.parseString(message).getAsJsonObject();
        System.out.println("client = " + object);

        UUID uuid = UUID.fromString(object.get("uuid").getAsString());
        RpcResponse<?> response = rpcClient.getRpcManager().getPendingCalls().remove(uuid);

        if (response != null) {

            if (object.has("response")) {
                processResponse(response, object.get("response").getAsString());
            } else {
                processResponse(response, null);
            }

        }

    }

    private <T> void processResponse(RpcResponse<T> response, String json) {

        CompletableFuture<T> future = response.getCompletableFuture();

        if (future != null) {

            if (json == null) {
                future.completeExceptionally(new Throwable("No response!"));
                return;
            }

            try {

                T answer = (T) Main.getGson().fromJson(json, response.getResult().getClass());
                future.complete(answer);

            } catch (Throwable e) {
                e.printStackTrace();
                future.completeExceptionally(new Throwable("No response!"));
            }

        }

    }

}
