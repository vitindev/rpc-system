package com.vitindev.packets;

import lombok.Getter;

@Getter
public class CalculatorResponse {

    private final int result;

    public CalculatorResponse(int result) {
        this.result = result;
    }

}
