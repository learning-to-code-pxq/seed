package com.tools.seed.system.service;

import java.util.Set;

public interface SysPermissionService {

    Set<String> getUserPerms(Long userId);

    Set<Long> getUserMenuIds(Long userId);
}
