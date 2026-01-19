package com.iviet.ivshs.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum AcSwing {
    ON("on"),
    OFF("off");

    private final String value;
}