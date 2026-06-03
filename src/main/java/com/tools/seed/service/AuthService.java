package com.tools.seed.service;

import com.tools.seed.common.dto.ChangePasswordRequest;
import com.tools.seed.common.dto.LoginRequest;
import com.tools.seed.common.dto.LoginResponse;
import com.tools.seed.common.dto.RegisterRequest;

public interface AuthService {

    LoginResponse login(LoginRequest request, String ip);

    void register(RegisterRequest request);

    void changePassword(Long userId, ChangePasswordRequest request);
}
