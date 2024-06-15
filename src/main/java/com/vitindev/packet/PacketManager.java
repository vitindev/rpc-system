package com.vitindev.packet;


import com.vitindev.Main;
import com.vitindev.exception.ErrorPacket;
import com.vitindev.exception.PacketErrorResponse;
import com.vitindev.exception.list.PacketException;
import com.vitindev.exception.list.TimeoutPacketException;
import com.vitindev.kryo.KryoUtils;
import com.vitindev.packet.intercept.PacketInterceptorManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SuppressWarnings("unchecked")
public class PacketManager {

    private final Map<UUID, PacketReply<?>> awaitResponses = new HashMap<>();

    public <T extends Packet> CompletableFuture<T> call(Packet packet, String channel, Class<T> classz) {

        final var uuid = UUID.randomUUID();
        final CompletableFuture<T> completableFuture = new CompletableFuture<>();

        /* Cria uma chamada sem resposta */
        final var packetCall = new PacketCall<>(uuid, null, System.currentTimeMillis(), channel, packet);
        /* Cria a espera da resposta da chamada feita acima, com timeout de 3s */
        final var response = new PacketReply<>(uuid, completableFuture.orTimeout(3, TimeUnit.SECONDS).handle((r, throwable) -> {

            if (throwable instanceof TimeoutException) {

                synchronized (awaitResponses) {
                    awaitResponses.remove(uuid);
                }

                throw new TimeoutPacketException(throwable);
            }

            if (throwable instanceof RuntimeException)
                throw (RuntimeException) throwable;

            return r;

        }));

        publish(packetCall, channel);

        synchronized (awaitResponses) {
            awaitResponses.put(uuid, response);
        }

        return response.future();
    }

    public void reply(PacketCall<?> oldPacketCall, Packet packet) {

        final var channel = oldPacketCall.channel();

        /* Cria uma nova chamada, clonando a antiga, mas adicionando o ID de quem respondeu */
        final var packetCall = new PacketCall<>(oldPacketCall.createdIdPacket(), UUID.randomUUID(), System.currentTimeMillis(), channel, packet);
        publish(packetCall, channel);

    }

    public <T extends Packet> void listenReply(PacketCall<T> call) {

        final var packetReply = (PacketReply<T>) awaitResponses.get(call.createdIdPacket());

        if (packetReply != null) {

            final var packet = call.packet();
            final var future = packetReply.future();

            if (packet instanceof ErrorPacket errorPacket) {
                future.completeExceptionally(errorPacket.createException());
            } else {
                future.complete(packet);
            }

        } else {
            System.out.println("[System RPC] - Nenhuma chamada aguardando retorno para esse ID.");
        }

    }

    public void listen(byte[] message, String channel) throws IOException {

        final PacketCall<?> packetCall = KryoUtils.deserialize(message);

        if (packetCall == null || !channel.equalsIgnoreCase(packetCall.channel()))
            return;

        final var packet = packetCall.packet();

        if (packet instanceof ErrorPacket && packetCall.replyIdPacket() == null) {
            System.out.println("[System RPC] - Um packet de erro foi gerado sem retorno.");
            return;
        }

        /* Aqui o pacote já foi respondido, então teoricamente ele voltou */
        if (packetCall.replyIdPacket() != null) {
            listenReply(packetCall);
            return;
        }

        /* Aqui o pacote chegou no server e vai ser processado */
        final var intercept = PacketInterceptorManager.getInterceptor(packetCall.packet().getClass());

        if (intercept != null) {

            intercept.forEach(packetIntercept -> {

                try {

                    final var response = packetIntercept.intercept(packetCall.packet());

                    if (response != null)
                        reply(packetCall, response);

                } catch (PacketException e) {
                    reply(packetCall, new PacketErrorResponse(e));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            });

        } else {
            System.out.println("[System RPC] - Um packet chegou e não havia interceptor para ele.");
        }

    }

    private void publish(PacketCall<?> call, String channel) {
        final var serializer = KryoUtils.serialize(call);
        Main.getConnectionRedisFactory().operation(jedis -> jedis.publish(channel.getBytes(StandardCharsets.UTF_8), serializer));
    }

}
