package com.vitindev;

import com.google.gson.Gson;
import com.vitindev.client.RpcClient;
import com.vitindev.packets.CalculatorResponse;
import com.vitindev.server.RpcServer;
import lombok.Getter;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Getter
public class Main {

    public static UUID baseUUID = UUID.randomUUID();

    @Getter
    private static final Gson gson = new Gson();

    public static void main(String[] args) {

        RpcClient client = new RpcClient();
        client.init();

        RpcServer server = new RpcServer();
        server.init();
        server.loadExecutors();

        CompletableFuture<CalculatorResponse> future = client.getRpcManager().call(new CalculatorResponse(0), baseUUID);

        future.thenAccept(calculatorResponse -> {

            try {

                System.out.println(calculatorResponse.getResult());

            } catch (Throwable e) {
                e.printStackTrace();
            }


        }).exceptionally(throwable -> {

            System.out.println(throwable.getMessage());

            return null;
        });


    }

}