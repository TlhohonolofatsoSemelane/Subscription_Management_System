package com.subtrack.client.service.impl;

import com.subtrack.client.dto.LoginRequest;
import com.subtrack.client.dto.RegisterRequest;
import com.subtrack.client.service.AuthClientService;
import com.subtrack.server.rmi.remote.AuthRemoteService;

public class AuthClientServiceImpl implements AuthClientService {

    private final AuthRemoteService authRemoteService;

    public AuthClientServiceImpl(AuthRemoteService authRemoteService) {
        this.authRemoteService = authRemoteService;
    }

    @Override
    public String register(RegisterRequest request) {
        try {
            if (request == null) return "REGISTER_FAIL:Request is null";
            return authRemoteService.register(
                    request.getUsername(),
                    request.getEmail(),
                    request.getPassword()
            );
        } catch (Exception e) {
            return "REGISTER_FAIL:" + e.getMessage();
        }
    }

    @Override
    public String login(LoginRequest request) {
        try {
            if (request == null) return "LOGIN_FAIL:Request is null";
            return authRemoteService.login(request.getUsername(), request.getPassword());
        } catch (Exception e) {
            return "LOGIN_FAIL:" + e.getMessage();
        }
    }

    @Override
    public String verifyOtp(String username, String otp) {
        try {
            return authRemoteService.verifyOtp(username, otp);
        } catch (Exception e) {
            return "OTP_FAIL:" + e.getMessage();
        }
    }
}
