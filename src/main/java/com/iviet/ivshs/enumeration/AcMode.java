package com.iviet.ivshs.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AcMode {
    COOL("cool"),
    HEAT("heat"),
    DRY("dry"),
    FAN("fan"),
    AUTO("auto");

    private final String value;
}