package com.vitindev.server;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vitindev.Main;
import com.vitindev.manager.Channels;
import com.vitindev.server.impl.Response;
import redis.clients.jedis.JedisPubSub;

import java.util.UUID;

public class PubSubServer extends JedisPubSub {

    private final RpcServer rpcServer;

    public PubSubServer(RpcServer rpcServer) {
        this.rpcServer = rpcServer;
    }

    @Override
    public void onMessage(String channel, String message) {

        if (!channel.equalsIgnoreCase(Channels.RPC_SERVER_SIDE.name()))
            return;

        JsonObject object = JsonParser.parseString(message).getAsJsonObject();
        System.out.println("server = " + object);

        String callName = object.get("call").getAsString();
        Response<?> processResponse = rpcServer.getExecutors().get(callName);

        if (processResponse != null) {

            UUID uuid = UUID.fromString(object.get("uuid").getAsString());
            Object response = processResponse.response(uuid);

            if (response == null) {
                System.out.println("NULL RESPONSE");
            } else {
                object.addProperty("response", Main.getGson().toJson(response));
            }

        }

        System.out.println("server = PASSED");
        rpcServer.getRpcManager().operation(jedis -> jedis.publish(Channels.RPC_CLIENT_SIDE.name(), object.toString()));
    }

}
