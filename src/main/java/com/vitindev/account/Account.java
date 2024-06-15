package com.vitindev.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@Getter
public class Account {

    private final UUID uuid;
    private final long id;
    private final String name;

    private final List<PermissionEntry> entries;

    private final Map<Long, GroupEntry> groups;

    private Preferences preferences;

}
