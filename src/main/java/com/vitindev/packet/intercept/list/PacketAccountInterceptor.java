package com.vitindev.packet.intercept.list;

import com.vitindev.account.*;
import com.vitindev.exception.list.PacketException;
import com.vitindev.exception.list.PlayerNotFoundException;
import com.vitindev.packet.intercept.PacketInterceptor;
import com.vitindev.packet.request.PacketAccountRequest;
import com.vitindev.packet.response.PacketAccountResponse;

import java.time.Instant;
import java.util.*;

public class PacketAccountInterceptor implements PacketInterceptor<PacketAccountRequest, PacketAccountResponse> {

    private final Map<Long, Account> accounts = new HashMap<>();

    public PacketAccountInterceptor() {

        final List<PermissionEntry> list = new ArrayList<>();

        for (int i = 0; i <= 4; i++)
            list.add(new PermissionEntry(i, "permission.add." + new Random().nextInt(100), Instant.now(), System.currentTimeMillis()));

        final Map<Long, GroupEntry> groups = new HashMap<>();

        for (int i = 0; i <= 4; i++)
            groups.put((long) i, new GroupEntry(i, Group.DEV, Instant.now(), System.currentTimeMillis()));

        accounts.put(1L, new Account(UUID.randomUUID(), 1, "VitinDev", list, groups));

        accounts.put(2L, new Account(UUID.randomUUID(), 2, "VitinDev2", list, groups));

        accounts.put(3L, new Account(UUID.randomUUID(), 3, "VitinDev3", list, groups));

    }

    @Override
    public PacketAccountResponse intercept(PacketAccountRequest packet) throws PacketException {

        final var id = packet.id();
        final var account = accounts.get(id);

        if (account == null) {
            throw new PlayerNotFoundException("Not Found Account;");
        } else {
            return new PacketAccountResponse(account);
        }

    }

}
