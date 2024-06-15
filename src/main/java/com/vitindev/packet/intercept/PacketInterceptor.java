package com.vitindev.packet.intercept;

import com.vitindev.exception.list.PacketException;
import com.vitindev.packet.Packet;

public interface PacketInterceptor<T extends Packet, R extends Packet> {

    R intercept(T packetCall) throws PacketException;

}
