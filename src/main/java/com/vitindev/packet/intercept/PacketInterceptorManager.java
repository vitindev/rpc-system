package com.vitindev.packet.intercept;

import com.vitindev.packet.Packet;
import com.vitindev.packet.intercept.list.PacketAccountInterceptor;
import com.vitindev.packet.request.PacketAccountRequest;
import com.vitindev.packet.request.PacketCalculatorRequest;
import com.vitindev.packet.response.PacketCalculatorResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("rawtypes")
public class PacketInterceptorManager {

    private final static Map<Class<? extends Packet>, List<PacketInterceptor>> interceptors = new HashMap<>();

    static {

        registerInterceptor(PacketCalculatorRequest.class, packet -> {

            final var x = packet.x();
            final var z = packet.z();
            final var operation = packet.operation();
            var result = 0.0F;

            switch (operation) {
                case "*" -> result = x * z;
                case "/" -> result = x / z;
                case "-" -> result = x - z;
                case "+" -> result = x + z;
            }

            return new PacketCalculatorResponse(result);

        });

        registerInterceptor(PacketAccountRequest.class, new PacketAccountInterceptor());

    }

    public static <T extends Packet> void registerInterceptor(Class<T> classz, PacketInterceptor<T, ?> intercept) {
        interceptors.computeIfAbsent(classz, k -> new ArrayList<>()).add(intercept);
    }

    public static List<PacketInterceptor> getInterceptor(Class<?> classz) {
        return interceptors.get(classz);
    }

}
