package com.vitindev.server.impl;

import java.util.UUID;

public interface Response<T> {

    T response(UUID uuid);

}
