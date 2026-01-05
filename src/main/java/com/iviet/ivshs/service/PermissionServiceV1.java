package com.iviet.ivshs.service;

import java.util.Set;

public interface PermissionServiceV1 {

    boolean hasPermission(Long clientId, String functionCode);

    Set<String> getPermissions(Long clientId);

    long countPermissions(Long clientId);
}
