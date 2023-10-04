package com.vitindev.client;

import com.vitindev.manager.Channels;
import com.vitindev.manager.RpcManager;
import com.vitindev.manager.ServerInterface;
import lombok.Getter;

@Getter
public class RpcClient implements ServerInterface {

    private final RpcManager rpcManager;

    public RpcClient() {
        rpcManager = new RpcManager();
    }

    @Override
    public void init() {
        rpcManager.connect();
        rpcManager.subscribe(new PubSubClient(this), Channels.RPC_CLIENT_SIDE.name());
    }

    @Override
    public void finish() {
        rpcManager.disconnect();
    }

}
