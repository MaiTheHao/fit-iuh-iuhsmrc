package com.iviet.ivshs.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum GatewayCommandV1 {

    ON("TRUE"),
    OFF("FALSE");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }
	
    @JsonCreator
    public static GatewayCommandV1 fromValue(String value) {
        return Arrays.stream(values())
                .filter(v -> v.value.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException("Unknown GatewayCommandV1: " + value)
                );
    }

    public static GatewayCommandV1 fromBoolean(boolean state) {
        return state ? ON : OFF;
    }
}
