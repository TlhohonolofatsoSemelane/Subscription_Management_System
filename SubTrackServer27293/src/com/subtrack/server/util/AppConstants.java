package com.subtrack.server.util;

public final class AppConstants {

    private AppConstants() {
        
    }

    // RMI
    public static final int RMI_PORT = 3333;
    public static final String RMI_HOST = "localhost";
    public static final String AUTH_SERVICE_NAME = "AuthService";
    
    public static final String SUBSCRIPTION_SERVICE_NAME = "SubscriptionService";


    
    public static final String MSG_INVALID_CREDENTIALS = "Invalid username or password.";
    public static final String MSG_USERNAME_EXISTS = "Username already exists.";
}
