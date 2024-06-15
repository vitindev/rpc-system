package com.vitindev.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Getter
public class Preferences {

    private final boolean test;
    private final Group highGroup;

    private final List<PermissionEntry> entries;
    private final Map<Long, GroupEntry> groups;

}
