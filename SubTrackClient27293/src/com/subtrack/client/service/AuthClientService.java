package com.subtrack.client.service;

import com.subtrack.client.dto.LoginRequest;
import com.subtrack.client.dto.RegisterRequest;

public interface AuthClientService {
    String register(RegisterRequest request);
    String login(LoginRequest request);

    
    String verifyOtp(String username, String otp);
}
