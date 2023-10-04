package com.vitindev.server.impl;

import com.vitindev.packets.CalculatorResponse;

import java.util.UUID;

public class CalculatorRequest implements Response<CalculatorResponse> {

    @Override
    public CalculatorResponse response(UUID uuid) {



        return new CalculatorResponse(5-4);
    }

}
