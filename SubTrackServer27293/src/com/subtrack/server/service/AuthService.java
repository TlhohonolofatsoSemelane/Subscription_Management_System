package com.subtrack.server.service;

import com.subtrack.server.model.User;
import com.subtrack.server.service.dto.LoginRequest;
import com.subtrack.server.service.dto.RegisterRequest;

public interface AuthService {
    User register(RegisterRequest request);

    // step 1
    String loginAndSendOtp(LoginRequest request);

    // step 2
    User verifyOtp(String username, String otp);
}
