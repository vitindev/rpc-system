package com.vitindev.account;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;

@RequiredArgsConstructor
@Getter
public class GroupEntry {

    private final long id;
    private final Group group;

    private final Instant expireAt;
    private final long updatedAt;

}
