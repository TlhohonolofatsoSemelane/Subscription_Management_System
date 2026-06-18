package com.subtrack.server.rmi.remote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface AuthRemoteService extends Remote {
    String register(String username, String email, String password) throws RemoteException;
    String login(String username, String password) throws RemoteException;

    // add this
    String verifyOtp(String username, String otp) throws RemoteException;
}
