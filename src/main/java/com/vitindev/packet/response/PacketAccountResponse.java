package com.vitindev.packet.response;

import com.vitindev.account.Account;
import com.vitindev.packet.Packet;

public record PacketAccountResponse(
        Account accountResult
) implements Packet {
}
