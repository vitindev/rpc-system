package com.vitindev.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@RequiredArgsConstructor
@Getter
public class PermissionEntry {

    private final long id;
    private final String permission;

    private final Instant expireAt;
    private final long updatedAt;

}
