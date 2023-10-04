package com.vitindev.server;

import com.vitindev.manager.Channels;
import com.vitindev.manager.RpcManager;
import com.vitindev.manager.ServerInterface;
import com.vitindev.packets.CalculatorResponse;
import com.vitindev.server.impl.CalculatorRequest;
import com.vitindev.server.impl.Response;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class RpcServer implements ServerInterface {

    private final RpcManager rpcManager;

    private final Map<String, Response<?>> executors;

    public RpcServer() {
        executors = new HashMap<>();
        rpcManager = new RpcManager();
    }

    @Override
    public void init() {
        rpcManager.connect();
        rpcManager.subscribe(new PubSubServer(this), Channels.RPC_SERVER_SIDE.name());
    }

    @Override
    public void finish() {
        rpcManager.disconnect();
    }

    public void loadExecutors() {
        executors.put(CalculatorResponse.class.getSimpleName(), new CalculatorRequest());
    }

}
