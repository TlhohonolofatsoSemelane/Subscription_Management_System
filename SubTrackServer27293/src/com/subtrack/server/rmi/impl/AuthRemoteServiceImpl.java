package com.subtrack.server.rmi.impl;

import com.subtrack.server.dao.UserDao;
import com.subtrack.server.dao.UserDaoImpl;
import com.subtrack.server.model.User;
import com.subtrack.server.rmi.remote.AuthRemoteService;
import com.subtrack.server.service.AuthService;
import com.subtrack.server.service.dto.LoginRequest;
import com.subtrack.server.service.dto.RegisterRequest;
import com.subtrack.server.service.impl.AuthServiceImpl;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class AuthRemoteServiceImpl extends UnicastRemoteObject implements AuthRemoteService {

    private final AuthService authService;

    public AuthRemoteServiceImpl() throws RemoteException {
        super();
        UserDao userDao = new UserDaoImpl();
        this.authService = new AuthServiceImpl(userDao);
    }

    @Override
    public String register(String username, String email, String password) throws RemoteException {
        try {
            User u = authService.register(new RegisterRequest(username, email, password));
            return "REGISTER_OK:" + u.getId() + ":" + u.getUsername();
        } catch (Exception e) {
            return "REGISTER_FAIL:" + e.getMessage();
        }
    }

    @Override
    public String login(String username, String password) throws RemoteException {
        try {
            System.out.println("[RMI] login request user=" + username);
            String res = authService.loginAndSendOtp(new LoginRequest(username, password));
            System.out.println("[RMI] login response => " + res);
            return res;
        } catch (Exception e) {
            return "LOGIN_FAIL:" + e.getMessage();
        }
    }

    @Override
    public String verifyOtp(String username, String otp) throws RemoteException {
        try {
            System.out.println("[RMI] verifyOtp request user=" + username + ", otp=" + otp);
            User u = authService.verifyOtp(username, otp);
            String res = "LOGIN_OK:" + u.getId() + ":" + u.getUsername();
            System.out.println("[RMI] verifyOtp response => " + res);
            return res;
        } catch (Exception e) {
            return "OTP_FAIL:" + e.getMessage();
        }
    }
}
