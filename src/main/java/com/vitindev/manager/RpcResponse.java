package com.vitindev.manager;

import lombok.Getter;

import java.util.concurrent.CompletableFuture;

@Getter
public class RpcResponse<T> {

    private final T result;

    private final CompletableFuture<T> completableFuture;

    public RpcResponse(T result) {
        this.result = result;
        this.completableFuture = new CompletableFuture<>();
    }

}
