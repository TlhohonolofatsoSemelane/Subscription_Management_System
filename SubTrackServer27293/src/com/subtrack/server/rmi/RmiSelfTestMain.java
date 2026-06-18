package com.subtrack.server.rmi;

import com.subtrack.server.rmi.remote.AuthRemoteService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RmiSelfTestMain {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            AuthRemoteService auth = (AuthRemoteService) registry.lookup("AuthService");

            String reg = auth.register("proof_user", "proof_user@mail.com", "123456");
            System.out.println("REGISTER RESULT = " + reg);

            String login = auth.login("proof_user", "123456");
            System.out.println("LOGIN RESULT = " + login);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
